import React, { useState, useEffect, useMemo } from "react";
import axios from "axios";
import { AlertType } from "../../common/Alert/AlertDialog";
import { useAlert } from "../../common/Alert/useAlert";
import styles from "./SakshiEnrolledLearningObjects.module.css";
import { getALMConfig } from "../../utils/global";
import { QueryParams, RestAdapter } from "../../utils/restAdapter";
import { JsonApiParse } from "../../utils/jsonAPIAdapter";
import { JsonApiResponse, PrimeLearningObject, PrimeLearningObjectInstance } from "../../models";

// Helper function outside component to avoid recreation
function getCookieByName(name: string): string | null {
  const cookies = document.cookie.split(";");
  for (let cookie of cookies) {
    cookie = cookie.trim();
    if (cookie.startsWith(name + "=")) {
      return cookie.substring(name.length + 1);
    }
  }
  return null;
}

const SakshiEnrolledLearningObjects = () => {
  const [userId, setUserId] = useState<string | null>(null);
  const [learningObjects, setLearningObjects] = useState<PrimeLearningObject[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [apiCallInProgress, setApiCallInProgress] = useState<boolean>(false);
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [almAlert] = useAlert();

  useEffect(() => {
    const getEnrolledLearningObjects = async (id: string): Promise<JsonApiResponse | undefined> => {
      try {
        const baseApiUrl = getALMConfig().primeApiURL;
        const params: QueryParams = 
        {   userId: id,
            'filter.loTypes': 'course',
            'page[limit]': '100',
            //'filter.learnerState': 'enrolled',
            'sort': 'name',
            'filter.ignoreEnhancedLP': 'true'
         };

        const response = await RestAdapter.get({
          url: `${baseApiUrl}/learningObjects?`,
          params
        });
        return JsonApiParse(response);
      } catch (e) {
        setError(`Error loading learning objects: ${e}`);
        console.error("Error while loading learning objects:", e);
      }
    };

    const fetchData = async () => {
      setIsLoading(true);
      try {
        const cookieValue = getCookieByName("user");
        
        if (!cookieValue) {
          setError("User data not found");
          setIsLoading(false);
          return;
        }
        
        const parsedJson = JSON.parse(cookieValue);
        
        if (parsedJson?.data?.id) {
          const userId = parsedJson.data.id;
          setUserId(userId);
          
          const response = await getEnrolledLearningObjects(userId);
          
          if (response?.learningObjectList) {
            setLearningObjects(response.learningObjectList);
          } else {
            setLearningObjects([]);
          }
        } else {
          setError("User ID not found in the cookie data");
        }
      } catch (error) {
        setError(`Error processing data: ${error}`);
        console.error("Error in fetchData:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, []);

  // Memoize the course grid to prevent unnecessary re-renders
  const courseGrid = useMemo(() => {
    return learningObjects.map((course: PrimeLearningObject, index: number) => {
      // Skip if no localized metadata is available
      if (!course.localizedMetadata || !course.localizedMetadata[0]) return null;
      
      const metadata = course.localizedMetadata[0];
      const instanceId = course.instances && course.instances[0] ? course.instances[0].id : "";
      const courseUrl = `http://localhost:4505/content/learning/language-masters/en/overview.html/trainingId/${course.id}/trainingInstanceId/${instanceId}/home.html`;
      
      const fallbackImage = "https://img.freepik.com/free-vector/online-certification-illustration_23-2148575636.jpg?semt=ais_hybrid&w=740";
      
      // Function to check course availability
      const checkCourseAvailability = async (courseId: string, event: React.MouseEvent) => {
        if (apiCallInProgress) {
          return;
        }
        
        // Prevent the default link behavior
        event.preventDefault();
        
        setApiCallInProgress(true);
        
        try {
          // Get the OAuth token from cookies
          const almCpToken = getCookieByName("alm_cp_token");
          
          if (!almCpToken) {
            almAlert(true, "AUTHENTICATION TOKEN NOT FOUND", AlertType.error, true);
            setApiCallInProgress(false);
            return;
          }
          
          // Call the API with the course ID and authorization header
          const apiUrl = `https://learningmanager.adobe.com/primeapi/v2/learningObjects/${encodeURIComponent(courseId)}`;
          
          const response = await axios.get(apiUrl, {
            headers: {
              'Accept': 'application/vnd.api+json',
              'Authorization': `oauth ${almCpToken}`
            }
          });
          
          // Check if enrollment is present in the response
          const courseName = response.data.data.attributes.localizedMetadata[0].name;
          if (response.status === 200 && response.data.data.relationships.enrollment) {
            almAlert(true, `COURSE ${courseName} IS AVAILABLE FOR YOU!`, AlertType.success, true);
          } else {
            almAlert(true, `COURSE ${courseName} IS NOT AVAILABLE FOR YOU!`, AlertType.error, true);
          }
        } catch (error) {
          // Display failure popup if API call fails
          almAlert(true, "FAILED TO VERIFY COURSE AVAILABILITY", AlertType.error, true);
          console.error("Error checking course availability:", error);
        } finally {
          setApiCallInProgress(false);
        }
      };

      return (
        <div key={index} className={styles.courseTile}>
          <a 
            href={courseUrl} 
            className={styles.courseLink}
            onClick={(e) => checkCourseAvailability(course.id, e)}
          >
            <div className={styles.imageBanner}>
              <img 
                src={course.imageUrl || fallbackImage} 
                alt={metadata.name || "Course image"} 
                className={styles.courseImage}
                onError={(e) => {
                  e.currentTarget.src = fallbackImage;
                }}
              />
            </div>
            <div className={styles.courseInfo}>
              <h3 className={styles.courseTitle}>{metadata.name || "Untitled Course"}</h3>
              <p className={styles.courseDescription}>
                {metadata.description ? 
                  (metadata.description.length > 100 ? 
                    `${metadata.description.substring(0, 100)}...` : 
                    metadata.description) : 
                  "No description available"}
              </p>
            </div>
          </a>
        </div>
      );
    });
  }, [learningObjects]);

  // Filter courses based on search term
  const filteredCourses = useMemo(() => {
    if (!searchTerm.trim()) return learningObjects;
    
    return learningObjects.filter(course => {
      if (!course.localizedMetadata || !course.localizedMetadata[0]) return false;
      const metadata = course.localizedMetadata[0];
      
      return (
        metadata.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        metadata.description?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    });
  }, [learningObjects, searchTerm]);

  // UI rendering based on component state
  return (
    <div className={styles.container}>
      <h2>My Enrolled Learning Objects</h2>
      
      {isLoading ? (
        <div className={styles.loadingMessage}>Loading your learning objects...</div>
      ) : error ? (
        <div className={styles.errorMessage}>
          <span className={styles.errorIcon}>⚠️</span>
          {error}
        </div>
      ) : (
        <>
          {learningObjects.length > 0 && (
            <div className={styles.searchContainer}>
              <input
                type="text"
                placeholder="Search courses..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className={styles.searchInput}
              />
              {searchTerm && (
                <span className={styles.resultsCount}>
                  Found {filteredCourses.length} {filteredCourses.length === 1 ? 'course' : 'courses'}
                </span>
              )}
            </div>
          )}
          
          {learningObjects.length > 0 ? (
            filteredCourses.length > 0 ? (
              <div className={styles.courseGrid}>
                {filteredCourses.map((course: PrimeLearningObject, index: number) => {
                  // Skip if no localized metadata is available
                  if (!course.localizedMetadata || !course.localizedMetadata[0]) return null;
                  
                  const metadata = course.localizedMetadata[0];
                  const instanceId = course.instances && course.instances[0] ? course.instances[0].id : "";
                  const courseUrl = `http://localhost:4505/content/learning/language-masters/en/overview.html/trainingId/${course.id}/trainingInstanceId/${instanceId}/home.html`;
                  
                  const fallbackImage = "https://img.freepik.com/free-vector/online-certification-illustration_23-2148575636.jpg?semt=ais_hybrid&w=740";
                  
                  // Function to check course availability
                  const checkCourseAvailability = async (courseId: string, event: React.MouseEvent) => {
                    if (apiCallInProgress) {
                      return;
                    }
                    
                    // Prevent the default link behavior
                    event.preventDefault();
                    
                    setApiCallInProgress(true);
                    
                    try {
                      // Get the OAuth token from cookies
                      const almCpToken = getCookieByName("alm_cp_token");
                      
                      if (!almCpToken) {
                        almAlert(true, "AUTHENTICATION TOKEN NOT FOUND", AlertType.error, true);
                        setApiCallInProgress(false);
                        return;
                      }
                      
                      // Call the API with the course ID and authorization header
                      const apiUrl = `https://learningmanager.adobe.com/primeapi/v2/learningObjects/${encodeURIComponent(courseId)}`;
                      
                      const response = await axios.get(apiUrl, {
                        headers: {
                          'Accept': 'application/vnd.api+json',
                          'Authorization': `oauth ${almCpToken}`
                        }
                      });
                      
                      // Check if enrollment is present in the response
                      const courseName = response.data.data.attributes.localizedMetadata[0].name;
                      if (response.status === 200 && response.data.data.relationships.enrollment) {
                        almAlert(true, `COURSE ${courseName} IS AVAILABLE FOR YOU!`, AlertType.success, true);
                      } else {
                        almAlert(true, `COURSE ${courseName} IS NOT AVAILABLE FOR YOU!`, AlertType.error, true);
                      }
                    } catch (error) {
                      // Display failure popup if API call fails
                      almAlert(true, "FAILED TO VERIFY COURSE AVAILABILITY", AlertType.error, true);
                      console.error("Error checking course availability:", error);
                    } finally {
                      setApiCallInProgress(false);
                    }
                  };
                  
                  return (
                    <div key={index} className={styles.courseTile}>
                      <a 
                        href={courseUrl} 
                        className={styles.courseLink}
                        onClick={(e) => checkCourseAvailability(course.id, e)}
                      >
                        <div className={styles.imageBanner}>
                          <img 
                            src={course.imageUrl || fallbackImage} 
                            alt={metadata.name || "Course image"} 
                            className={styles.courseImage}
                            onError={(e) => {
                              e.currentTarget.src = fallbackImage;
                            }}
                          />
                        </div>
                        <div className={styles.courseInfo}>
                          <h3 className={styles.courseTitle}>{metadata.name || "Untitled Course"}</h3>
                          <p className={styles.courseDescription}>
                            {metadata.description ? 
                              (metadata.description.length > 100 ? 
                                `${metadata.description.substring(0, 100)}...` : 
                                metadata.description) : 
                              "No description available"}
                          </p>
                        </div>
                      </a>
                    </div>
                  );
                })}
              </div>
            ) : (
              <p className={styles.noCoursesMessage}>No courses match your search criteria.</p>
            )
          ) : (
            <p className={styles.noCoursesMessage}>No enrolled courses found.</p>
          )}
        </>
      )}
    </div>
  );
};

export default SakshiEnrolledLearningObjects;