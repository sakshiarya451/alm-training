/**
Copyright 2021 Adobe. All rights reserved.
This file is licensed to you under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under
the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
OF ANY KIND, either express or implied. See the License for the specific language
governing permissions and limitations under the License.
*/

import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useIntl } from "react-intl";
import { useDispatch } from "react-redux";
import { AlertType } from "../../common/Alert/AlertDialog";
import { useAlert } from "../../common/Alert/useAlert";
import APIServiceInstance from "../../common/APIService";
import {
  PrimeLearningObject,
  PrimeLearningObjectInstance,
  PrimeLearningObjectInstanceEnrollment,
  PrimeLearningObjectResource,
  PrimeLoInstanceSummary,
  PrimeNote,
} from "../../models/PrimeModels";
import { getJobaidUrl, isJobaidContentTypeUrl } from "../../utils/catalog";
import {
  COURSE,
  WAITING,
  RETIRED,
  MANAGER_APPROVAL,
  ACTIVE,
  PROD_PREHIRE_LXP_URL,
  QA_OR_ACCEPTANCE_LXP_URL,
} from "../../utils/constants";
import {
  getAccessToken,
  getALMAccount,
  getALMConfig,
  getALMObject,
  getALMUser,
  getCookieByName,
  getPageAttributes,
  isUserLoggedIn,
  setUserCookie,
} from "../../utils/global";
import {
  filterTrainingInstance,
  getActiveEnrollmentInstance,
  getEnrolledInstancesCount,
  getLocale,
  popFromParentPathStack,
  useBadge,
  useCardIcon,
  useLocalizedMetaData,
  useTrainingSkills,
} from "../../utils/hooks";
import { JsonApiParse } from "../../utils/jsonAPIAdapter";
import { LaunchPlayer } from "../../utils/playback-utils";
import { QueryParams, RestAdapter } from "../../utils/restAdapter";
import { GetTranslation } from "../../utils/translationService";
import axios from "axios";
import axiosRetry from "axios-retry";
import { checkIsEnrolled } from "../../utils/overview";
import { checkIfCompletionDeadlineNotPassed } from "../../utils/instance";

// const DEFAULT_INCLUDE_LO_OVERVIEW =
//   "instances.enrollment.loResourceGrades,enrollment.loInstance.loResources.resources,prerequisiteLOs,subLOs.prerequisiteLOs,subLOs.subLOs.prerequisiteLOs,authors,subLOs.enrollment.loResourceGrades, subLOs.subLOs.enrollment.loResourceGrades, subLOs.subLOs.instances.loResources.resources, subLOs.instances.loResources.resources,instances.loResources.resources,supplementaryLOs.instances.loResources.resources,supplementaryResources,subLOs.supplementaryResources,subLOs.enrollment,instances.badge,skills.skillLevel.badge,skills.skillLevel.skill,instances.loResources.resources.room,subLOs.enrollment.loInstance.loResources.resources,prerequisiteLOs.enrollment";

const COURSE_DEFAULT_INCLUDE_LO_OVERVIEW =
  "instances.enrollment.loResourceGrades, enrollment.loInstance.loResources.resources, prerequisiteLOs, authors, supplementaryLOs.instances.loResources.resources, supplementaryResources,prerequisiteLOs.enrollment";

const DEFAULT_INCLUDE_LO_OVERVIEW =
  "instances.enrollment.loResourceGrades,enrollment.loInstance.loResources.resources,prerequisiteLOs,subLOs.prerequisiteLOs,subLOs.subLOs.prerequisiteLOs,authors,subLOs.enrollment.loResourceGrades, subLOs.subLOs.enrollment.loResourceGrades, subLOs.subLOs.instances.loResources.resources, subLOs.instances.loResources.resources,supplementaryLOs.instances.loResources.resources,supplementaryResources,subLOs.supplementaryResources,subLOs.enrollment,instances.loResources.resources.room,subLOs.enrollment.loInstance.loResources.resources,prerequisiteLOs.enrollment";

export const useTrainingPage = (
  trainingId: string,
  instanceId: string = "",
  params: QueryParams = {},
  shouldSkipLOCalls: boolean = false
) => {
  const [trainingOverviewAttributes, setTrainingOverviewAttributes] = useState(
    () =>
      getPageAttributes("trainingOverviewPage", "trainingOverviewAttributes")
  );
  const baseApiUrl = getALMConfig().primeApiURL;
  const headers = { "content-type": "application/json" };
  const updateTrainingOverviewAttributes = getPageAttributes(
    "trainingOverviewPage",
    "trainingOverviewAttributes"
  );
  const { formatMessage, locale } = useIntl();
  const [almAlert] = useAlert();

  const [currentState, setCurrentState] = useState({
    trainingInstance: {} as PrimeLearningObjectInstance,
    isPreviewEnabled: false,
    isLoading: true,
    errorCode: "",
  });
  const { trainingInstance, isPreviewEnabled, isLoading, errorCode } =
    currentState;
  const [instanceSummary, setInstanceSummary] = useState(
    {} as PrimeLoInstanceSummary
  );
  // const [notes, setNotes] = useState([] as PrimeNote[]);
  const [lastPlayingLoResourceId, setLastPlayingLoResourceId] = useState("");
  const [refreshNotes, setRefreshNotes] = useState(false);
  const [refreshTraining, setRefreshTraining] = useState(false);
  const [refreshSelectedInstances, setRefreshSelectedInstances] =
    useState(false);
  const training = trainingInstance.learningObject;
  const [waitlistPosition, setWaitlistPosition] = useState("");
  const [selectedInstanceInfo, setSelectedInstanceInfo] = useState({} as any);
  const [selectedLoList, setSelectedLoList] = useState({} as any);
  const isInitialRender = useRef(true);
  const dispatch = useDispatch();

  const trainingAccounts = ["10768", "10814", "2136"];
  const preHireAccounts = ["11129", "11130", "2220"];
  const accountId = getALMConfig().accountId;
  const [privacyAccepted, setPrivacyAccepted] = useState(false)
  var loAccessErrorMessage = "";
  var loRetiredMessage = "";
  switch (accountId) {
    case "10738":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.employeeGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.employeeRetiredMessage"
      );
      break;
    case "10770":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildRetiredMessage"
      );
      break;
    case "10771":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildAdultGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildAdultRetiredMessage"
      );
      break;
    case "10768":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.trainingGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.trainingRetiredMessage"
      );
      break;
    case "10813":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.employeeGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.employeeRetiredMessage"
      );
      break;
    case "10814":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.trainingGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.trainingRetiredMessage"
      );
      break;
    case "10815":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildRetiredMessage"
      );
      break;
    case "10816":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildAdultGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildAdultRetiredMessage"
      );
      break;
    case "2133":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.employeeGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.employeeRetiredMessage"
      );
      break;
    case "2136":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.trainingGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.trainingRetiredMessage"
      );
      break;
    case "2134":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildRetiredMessage"
      );
      break;
    case "2135":
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildAdultGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.skillsbuildAdultRetiredMessage"
      );
      break;
    case "2196": // performace instance
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.genericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.genericRetiredMessage"
      );
      break;
    case "1926": // performace instance
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.genericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.genericRetiredMessage"
      );
      break;
    case "11129": // pre-hire dev
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.startatibmGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.startatibmRetiredAcceptanceMessage"
      );
      break;
    case "2220": // pre-hire prod
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.startatibmGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.startatibmRetiredMessage"
      );
      break;
    case "11130": // pre-hire acceptance
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.startatibmGenericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.startatibmRetiredAcceptanceMessage"
      );
      break;
    default:
      loAccessErrorMessage = GetTranslation(
        "alm.courseLaunch.genericErrorMessage"
      );
      loRetiredMessage = GetTranslation(
        "alm.courseLaunch.genericRetiredMessage"
      );
      break;
  }

  function getEnvironmentBasedLXPRedirectURL(accountId: string) {
    const preHireLXPredirectUrl = ['11130', '11129'].includes(accountId) ? QA_OR_ACCEPTANCE_LXP_URL : PROD_PREHIRE_LXP_URL;
    return preHireLXPredirectUrl;
  }

  useEffect(() => {
    async function preHirePrivacyConsentFlow() {
      try {
        const accountId = getALMConfig().accountId;
        if (!preHireAccounts.includes(accountId)) {
          // if user is not pre hire then page will load with normal flow
          setPrivacyAccepted(true);
          return;
        }
        if (!isUserLoggedIn()) {
          return;
        }
        const userResponseJSON = await getALMObject().getALMUser();
        const userResponse = JSON.parse(userResponseJSON);
        const tokenAPI = await APIServiceInstance.getAccessTokenAPI();
        const email = userResponse?.data?.attributes?.email;
        const token = tokenAPI?.data?.token;
        const privacyResponse = await APIServiceInstance.privacyAcceptanceAPI(email, token);
        if (privacyResponse.status >= 400) {
          throw new Error('alm.courseLaunch.startatibmGenericErrorMessage');
        }
        const privacyResponseData = await privacyResponse.json();
        const { consentRequired, consentProvided } = privacyResponseData?.data || {};
        if (consentRequired && !consentProvided) {
          // redirect the page to LXP page if consent is required and not provided
          const redirectURL = getEnvironmentBasedLXPRedirectURL(accountId);
          window.location.href = redirectURL;
          return;
        }
        else {
          // if consent is already provided then page will continue with normal flow.
          setPrivacyAccepted(true);
        }
      }
      catch (error: any) {
        almAlert(true, GetTranslation(error?.message), AlertType.error, false);
        return;
      }
    }
    preHirePrivacyConsentFlow();
  }, [])

  const sendInstanceId = useCallback(
    (
      selectedInstanceId: string,
      courseId: string,
      courseName: string,
      isbuttonChange: boolean,
      instanceName: string,
      contentModuleDuration: number,
      isCompleted = false
    ) => {
      setSelectedInstanceInfo((prevSelectedInstanceInfo: any) => {
        return {
          ...prevSelectedInstanceInfo,
          [courseId]: {
            instanceId: selectedInstanceId,
            name: courseName,
            isbuttonChange: isbuttonChange,
            instanceName: instanceName,
            courseDuration: contentModuleDuration,
          },
        };
      });

      if (isCompleted) {
        // Initialisation - checking completed lists
        const temp2 = { ...selectedLoList };
        temp2[courseId] = {
          instanceId: selectedInstanceId,
          name: courseName,
          isbuttonChange: isbuttonChange,
          instanceName: instanceName,
          courseDuration: contentModuleDuration,
        };
        setSelectedLoList({ ...temp2 });
      }
    },
    [selectedInstanceInfo]
  );
  const setInstancesForFlexLPOnLoad = useCallback(
    (selectedInstancesInfo) => {
      setSelectedInstanceInfo({ ...selectedInstancesInfo });
    },
    [selectedInstanceInfo]
  );

  useEffect(() => {
    if (!privacyAccepted) return;
    let userType = "";
    const validateLOResponse = async () => {
      if (isUserLoggedIn() && trainingId && instanceId) {
        const userResponse = await getALMObject().getALMUser();
        const almBaseURL = getALMConfig().almBaseURL;
        const accountId = getALMConfig().accountId;
        const validateLOPostParams = {
          userData: JSON.parse(userResponse),
          accountId: accountId,
          almBaseURL: almBaseURL,
          loId: trainingId,
        };
        try {
          const customAxios = axios.create();
          axiosRetry(customAxios, {
            retries: 2, // Number of retry attempts
            retryCondition: (error) => {
              // Retry on network errors or 5xx status codes
              return (
                axiosRetry.isNetworkOrIdempotentRequestError(error) ||
                (error.response && error.response.status >= 500) ||
                false
              );
            },
          });
          customAxios
            .post(
              `${window.location.origin}/bin/alm/validateLOResponse`,
              validateLOPostParams
            )
            .then(function (response) {
              if (response.data) {
                const loResponseAsAdmin = response.data.loResponseAsAdmin;
                if (loResponseAsAdmin === "") {
                  almAlert(true, loAccessErrorMessage, AlertType.error, false);
                  return;
                } else {
                  const loResponse = JSON.parse(loResponseAsAdmin);
                  const loState = loResponse?.data?.attributes?.state;
                  if (loState === "Expired") {
                    almAlert(true, loRetiredMessage, AlertType.error, false);
                    return;
                  }
                }
                const updatedUserType = response.data.updatedUserType;
                if (updatedUserType !== "") {
                  userType = response.data.updatedUserType;
                  let userCookieData = JSON.parse(getCookieByName("user"));
                  if (
                    typeof userCookieData.data.attributes.fields === "undefined"
                  ) {
                    userCookieData.data.attributes.fields = {};
                  }
                  userCookieData.data.attributes.fields.userType = userType;
                  setUserCookie(JSON.stringify(userCookieData));
                }
              } else {
                almAlert(true, loAccessErrorMessage, AlertType.error, false);
                return;
              }
              getLoResponseAsLearner({ userType });
            })
            .catch(function (error) {
              console.log("validateLOResponse error", error);
              if (error.response && error.response.status >= 500) {
                almAlert(
                  true,
                  GetTranslation("alm.courseLaunch.apiFailErrorMessage"),
                  AlertType.error,
                  false
                );
              }
              else {
                const userResponseParsed = JSON.parse(userResponse);
                const email: string = userResponseParsed?.data?.attributes?.email;
                let errorMessage = loAccessErrorMessage;
                if (email.includes("guest"))
                  errorMessage = GetTranslation("alm.courseLaunch.guestAccessErrorMessage");

                almAlert(true, errorMessage, AlertType.error, false);
              }
            });
        } catch (error) {
          console.error(
            "Error getting loResponseAsAdmin or UpdateBPUserType:",
            error
          );
          almAlert(
            true,
            GetTranslation("alm.courseLaunch.apiFailErrorMessage"),
            AlertType.error,
            false
          );
        }
      }
    };
    validateLOResponse();

    const getLoResponseAsLearner = async ({ userType = "" } = {}) => {
      try {
        let queryParam: QueryParams = {};
        let includeParams = "";
        if (trainingId.includes("course:")) {
          includeParams = COURSE_DEFAULT_INCLUDE_LO_OVERVIEW;
        } else {
          includeParams = DEFAULT_INCLUDE_LO_OVERVIEW;
        }
        queryParam["include"] = includeParams;
        queryParam["useCache"] = true;
        queryParam["filter.ignoreEnhancedLP"] = false;
        const account = await getALMAccount();
        const accountId = getALMConfig().accountId;
        const response = await APIServiceInstance.getTraining(
          trainingId,
          queryParam
        );

        if (response) {
          const authorNames = response.authorNames?.length
            ? response.authorNames.join(", ")
            : "";
          if (response.enrollmentType === MANAGER_APPROVAL) {
            if (
              response.multienrollmentEnabled &&
              getEnrolledInstancesCount(response) > 1
            ) {
              // get active enrollment
              let activeInstanceId = getActiveEnrollmentInstance(response);
              instanceId = activeInstanceId ? activeInstanceId : instanceId;
            } else if (response.enrollment && response.enrollment.loInstance) {
              instanceId = response.enrollment.loInstance.id;
            } else {
              let selfEnrollInMAErrorMessage = GetTranslation(
                "alm.courseLaunch.selfEnrollInMAErrorMessage"
              ).replace("{Author}", authorNames);
              almAlert(
                true,
                selfEnrollInMAErrorMessage,
                AlertType.error,
                false
              );
              return;
            }
          }
          const trainingInstance = filterTrainingInstance(response, instanceId);

          if (trainingInstance.state === RETIRED) {
            setCurrentState({
              trainingInstance: {} as PrimeLearningObjectInstance,
              isPreviewEnabled: false,
              isLoading: true,
              errorCode: "retired",
            });
            if (
              trainingInstance.learningObject?.enrollmentType ===
              MANAGER_APPROVAL
            ) {
              let instanceExpiredMAErrorMessage = GetTranslation(
                "alm.courseLaunch.instanceExpiredMAErrorMessage"
              ).replace("{Author}", authorNames);
              almAlert(
                true,
                instanceExpiredMAErrorMessage,
                AlertType.error,
                false
              );
              return;
            } else {
              almAlert(true, loRetiredMessage, AlertType.error, false);
              return;
            }
          } else if (
            trainingInstance.learningObject?.enrollmentType ===
            MANAGER_APPROVAL &&
            trainingInstance.state === ACTIVE &&
            !checkIfCompletionDeadlineNotPassed(trainingInstance)
          ) {
            setCurrentState({
              trainingInstance: {} as PrimeLearningObjectInstance,
              isPreviewEnabled: false,
              isLoading: true,
              errorCode: "deadline passed",
            });
            let instanceExpiredMAErrorMessage = GetTranslation(
              "alm.courseLaunch.instanceExpiredMAErrorMessage"
            ).replace("{Author}", authorNames);
            almAlert(
              true,
              instanceExpiredMAErrorMessage,
              AlertType.error,
              false
            );
            return;
          } else {
            setCurrentState({
              trainingInstance,
              isPreviewEnabled: account.enableModulePreview || false,
              isLoading: false,
              errorCode: "",
            });

            if (!checkIsEnrolled(trainingInstance.learningObject?.enrollment)) {
              if (
                trainingInstance.learningObject?.enrollmentType ===
                MANAGER_APPROVAL
              ) {
                let selfEnrollInMAErrorMessage = GetTranslation(
                  "alm.courseLaunch.selfEnrollInMAErrorMessage"
                ).replace("{Author}", authorNames);
                almAlert(
                  true,
                  selfEnrollInMAErrorMessage,
                  AlertType.error,
                  false
                );
                return;
              } else {
                enrollmentHandler({ id: trainingId, instanceId: instanceId });
              }
            }
          }
          setRefreshSelectedInstances((prevState) => !prevState);
        } else {
          if (userType !== "" && trainingAccounts.includes(accountId)) {
            if (userType === "IBMer") {
              almAlert(
                true,
                GetTranslation("alm.courseLaunch.ibmerAccessErrorMessage"),
                AlertType.error,
                false
              );
            } else if (userType === "BusinessPartner") {
              almAlert(
                true,
                GetTranslation("alm.courseLaunch.bpAccessErrorMessage"),
                AlertType.error,
                false
              );
            } else {
              almAlert(
                true,
                GetTranslation("alm.courseLaunch.clientAccessErrorMessage"),
                AlertType.error,
                false
              );
            }
          } else {
            almAlert(true, loAccessErrorMessage, AlertType.error, false);
          }
        }
      } catch (error: any) {
        console.log("Error while loading training ", error);
        setCurrentState({
          trainingInstance: {} as PrimeLearningObjectInstance,
          isPreviewEnabled: false,
          isLoading: false,
          errorCode: error.status,
        });
        if (error.status === 401) {
          window.location.reload();
        }
        else if (error.response && error.response.status >= 500){
          almAlert(
            true,
            GetTranslation("alm.courseLaunch.apiFailErrorMessage"),
            AlertType.error,
            false
          );
        }
        else if (userType !== "" && trainingAccounts.includes(accountId)) {
          if (userType === "IBMer") {
            almAlert(
              true,
              GetTranslation("alm.courseLaunch.ibmerAccessErrorMessage"),
              AlertType.error,
              false
            );
          } else if (userType === "BusinessPartner") {
            almAlert(
              true,
              GetTranslation("alm.courseLaunch.bpAccessErrorMessage"),
              AlertType.error,
              false
            );
          } else {
            almAlert(
              true,
              GetTranslation("alm.courseLaunch.clientAccessErrorMessage"),
              AlertType.error,
              false
            );
          }
        } else {
          almAlert(true, loAccessErrorMessage, AlertType.error, false);
        }
      }
    };
  }, [privacyAccepted]);

  useEffect(() => {
    if (!privacyAccepted) return;
    const updateUserLocale = async () => {
      const almAccessToken = getAccessToken();
      const userResponse = await getALMUser();
      const currentUserId = userResponse?.user?.id;
      const uiLocale = userResponse?.user?.uiLocale;
      const contentLocale = userResponse?.user?.contentLocale;
      if (
        almAccessToken === "" ||
        (uiLocale === locale && contentLocale === locale)
      ) {
        return;
      }
      const requestBody = {
        data: {
          id: currentUserId,
          type: "user",
          attributes: {
            uiLocale: locale,
            contentLocale: locale,
          },
        },
      };
      const headers = {
        "Authorization": `oauth ${almAccessToken}`,
        "content-type": "application/vnd.api+json;charset=UTF-8",
        "accept": "application/vnd.api+json",
      };
      const apiUrl = `${getALMConfig().primeApiURL}users/${currentUserId}`;
      try {
        axios
          .patch(apiUrl, requestBody, { headers })
          .then(function (response) {
            setUserCookie(JSON.stringify(response.data));
          })
          .catch(function (error) {
            console.log("update user locale error", error);
          });
      } catch (error) {
        console.error("Error updating user locale:", error);
      }
    };
    if (isUserLoggedIn()) {
      updateUserLocale();
    }
  }, [isUserLoggedIn(), locale, privacyAccepted]);

  useEffect(() => {
    if (!privacyAccepted) return;
    if (training && training.enrollment) {
      const temporary = { ...selectedInstanceInfo };
      training.subLOs?.forEach((item: any) => {
        if (
          item.enrollment &&
          item.enrollment.loInstance &&
          item.loType === COURSE
        ) {
          temporary[item.id] = {
            instanceId: item.enrollment?.loInstance.id,
            name: item.localizedMetadata[0].name,
            isbuttonChange: false,
            instanceName: item.enrollment?.loInstance.localizedMetadata[0].name,
            courseDuration: item.duration,
          };
        }
      });
      setSelectedInstanceInfo({ ...temporary });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isLoading, refreshSelectedInstances, privacyAccepted]);

  const [
    renderTrainingOverviewAttributes,
    setRenderTrainingOverviewAttributes,
  ] = useState(false);

  useEffect(() => {
    if (!privacyAccepted) return;
    if (training && !renderTrainingOverviewAttributes) {
      setTrainingOverviewAttributes(updateTrainingOverviewAttributes);
      setRenderTrainingOverviewAttributes(true);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [training, privacyAccepted]);

  useEffect(() => {
    if (!privacyAccepted) return;
    const getTrainingInstance = async () => {
      try {
        if (isInitialRender.current) {
          // Skip the first render
          isInitialRender.current = false;
          return;
        }
        let queryParam: QueryParams = {};
        let includeParams = "";
        if (trainingId.includes("course:")) {
          includeParams = COURSE_DEFAULT_INCLUDE_LO_OVERVIEW;
        } else {
          includeParams = DEFAULT_INCLUDE_LO_OVERVIEW;
        }
        queryParam["include"] = includeParams;
        queryParam["useCache"] = true;
        queryParam["filter.ignoreEnhancedLP"] = false;
        const account = await getALMAccount();
        const response = await APIServiceInstance.getTraining(
          trainingId,
          queryParam
        );

        if (response) {
          const authorNames = response.authorNames?.length
            ? response.authorNames.join(", ")
            : "";
          if (response.enrollmentType === MANAGER_APPROVAL) {
            if (
              response.multienrollmentEnabled &&
              getEnrolledInstancesCount(response) > 1
            ) {
              // get active enrollment
              let activeInstanceId = getActiveEnrollmentInstance(response);
              instanceId = activeInstanceId ? activeInstanceId : instanceId;
            } else if (response.enrollment && response.enrollment.loInstance) {
              instanceId = response.enrollment.loInstance.id;
            } else {
              let selfEnrollInMAErrorMessage = GetTranslation(
                "alm.courseLaunch.selfEnrollInMAErrorMessage"
              ).replace("{Author}", authorNames);
              almAlert(
                true,
                selfEnrollInMAErrorMessage,
                AlertType.error,
                false
              );
              return;
            }
          }
          const trainingInstance = filterTrainingInstance(response, instanceId);
          if (trainingInstance.state === RETIRED) {
            setCurrentState({
              trainingInstance: {} as PrimeLearningObjectInstance,
              isPreviewEnabled: false,
              isLoading: true,
              errorCode: "retired",
            });
            if (
              trainingInstance.learningObject?.enrollmentType ===
              MANAGER_APPROVAL
            ) {
              let instanceExpiredMAErrorMessage = GetTranslation(
                "alm.courseLaunch.instanceExpiredMAErrorMessage"
              ).replace("{Author}", authorNames);
              almAlert(
                true,
                instanceExpiredMAErrorMessage,
                AlertType.error,
                false
              );
              return;
            } else {
              almAlert(true, loRetiredMessage, AlertType.error, false);
              return;
            }
          } else if (
            trainingInstance.learningObject?.enrollmentType ===
            MANAGER_APPROVAL &&
            trainingInstance.state === ACTIVE &&
            !checkIfCompletionDeadlineNotPassed(trainingInstance)
          ) {
            setCurrentState({
              trainingInstance: {} as PrimeLearningObjectInstance,
              isPreviewEnabled: false,
              isLoading: true,
              errorCode: "deadline passed",
            });
            let instanceExpiredMAErrorMessage = GetTranslation(
              "alm.courseLaunch.instanceExpiredMAErrorMessage"
            ).replace("{Author}", authorNames);
            almAlert(
              true,
              instanceExpiredMAErrorMessage,
              AlertType.error,
              false
            );
            return;
          } else {
            setCurrentState({
              trainingInstance,
              isPreviewEnabled: account.enableModulePreview || false,
              isLoading: false,
              errorCode: "",
            });
          }
          setRefreshSelectedInstances((prevState) => !prevState);
        }
      } catch (error: any) {
        console.log("Error while loading training " + error);
        setCurrentState({
          trainingInstance: {} as PrimeLearningObjectInstance,
          isPreviewEnabled: false,
          isLoading: false,
          errorCode: error.status,
        });
        if (error.status === 401) {
          window.location.reload();
        }
        else if (error.response && error.response.status >= 500) {
          almAlert(
            true,
            GetTranslation("alm.courseLaunch.apiFailErrorMessage"),
            AlertType.error,
            false
          );
        }
        else{
          almAlert(true, loAccessErrorMessage, AlertType.error, false);
        }
      }
    };
    if (!shouldSkipLOCalls) {
      getTrainingInstance();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [refreshTraining, privacyAccepted]);

  useEffect(() => {
    if (!privacyAccepted) return;
    const getSummary = async () => {
      const response = await APIServiceInstance.getTrainingInstanceSummary(
        trainingInstance.learningObject.id,
        trainingInstance.id
      );
      if (response) {
        setInstanceSummary(response.loInstanceSummary);
      }

      try {
      } catch (error) { }
    };
    // if (trainingInstance?.id && !shouldSkipLOCalls) {
    //   getSummary();
    // }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [trainingInstance, privacyAccepted]);

  const getWaitlistPosition = async ({
    enrollmentId,
  }: {
    enrollmentId: string;
  }) => {
    try {
      let response = await RestAdapter.ajax({
        url: `${baseApiUrl}/enrollments/${enrollmentId}/waitlistPosition`,
        method: "GET",
        headers: headers,
      });

      if (response) {
        setWaitlistPosition(response as string);
      }
    } catch (e) {
      console.log(e);
    }
  };

  const enrollmentHandler = useCallback(
    async ({
      id,
      instanceId,
      isSupplementaryLO = false,
      allowMultiEnrollment = false,
      headers = {},
    } = {}): Promise<PrimeLearningObjectInstanceEnrollment> => {
      let queryParam: QueryParams = {
        loId: id || trainingId,
        loInstanceId: instanceId || trainingInstance.id,
        allowMultiEnrollment: allowMultiEnrollment,
      };
      const emptyResponse = {} as PrimeLearningObjectInstanceEnrollment;
      try {
        const response = await APIServiceInstance.enrollToTraining(
          queryParam,
          headers
        );
        if (!isSupplementaryLO) {
          //Refresh the training data
          setRefreshTraining((prevState) => !prevState);
          setRefreshNotes(true);
        }
        if (response) {
          return response.learningObjectInstanceEnrollment;
        }
        return emptyResponse;
      } catch (error) {
        almAlert(true, loAccessErrorMessage, AlertType.error);
        return emptyResponse;
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [trainingId, instanceId]
  );

  const flexLpEnrollHandler = useCallback(
    async ({
      id,
      instanceId,
      isSupplementaryLO = false,
      allowMultiEnrollment = false,
      body = {},
      headers = {},
    } = {}): Promise<PrimeLearningObjectInstanceEnrollment> => {
      let queryParam: QueryParams = {
        loId: id || trainingId,
        loInstanceId: instanceId || trainingInstance.id,
        allowMultiEnrollment: allowMultiEnrollment,
      };
      const emptyResponse = {} as PrimeLearningObjectInstanceEnrollment;
      try {
        const data = await RestAdapter.ajax({
          url: `${getALMConfig().primeApiURL}enrollments`,
          method: "POST",
          params: queryParam,
          body: JSON.stringify(body),
          headers: { ...headers, "content-type": "application/json" },
        });

        const response = JsonApiParse(data);

        if (!isSupplementaryLO) {
          //Refresh the training data
          setRefreshTraining((prevState) => !prevState);
          setRefreshNotes(true);
        }
        if (response) {
          return response.learningObjectInstanceEnrollment;
        }
        return emptyResponse;
      } catch (error) {
        almAlert(true, GetTranslation("alm.enrollment.error"), AlertType.error);
        return emptyResponse;
      }
    },
    [trainingId, trainingInstance.id]
  );

  const unEnrollmentHandler = useCallback(
    async ({
      enrollmentId,
      isFlexLp = false,
      isSupplementaryLO = false,
    } = {}) => {
      try {
        await APIServiceInstance.unenrollFromTraining(enrollmentId);
        if (!isSupplementaryLO) {
          //just to refresh the training data
          setRefreshTraining((prevState) => !prevState);
          if (isFlexLp) {
            setSelectedInstanceInfo({});
          }
          return true;
        }
      } catch (error) {
        almAlert(
          true,
          GetTranslation("alm.unenrollment.error"),
          AlertType.error
        );
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    []
  );

  const updateEnrollmentHandler = useCallback(
    async ({ enrollmentId, instanceEnrollList, isFlexLp = false }) => {
      const baseApiUrl = getALMConfig().primeApiURL;
      const headers = { "content-type": "application/json" };

      try {
        await RestAdapter.ajax({
          url: `${baseApiUrl}enrollments?enrollmentId=${enrollmentId}`,
          method: "PATCH",
          body: JSON.stringify(instanceEnrollList),
          headers: headers,
        });
        if (isFlexLp) {
          setSelectedInstanceInfo({});
        }
        setRefreshTraining((prevState) => !prevState);
        setRefreshNotes(true);
      } catch (error) {
        almAlert(true, GetTranslation("alm.enrollment.error"), AlertType.error);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [dispatch]
  );

  const addToCartHandler = useCallback(async (): Promise<{
    items: any;
    totalQuantity: Number;
    error: any;
  }> => {
    try {
      return await APIServiceInstance.addProductToCart(trainingInstance.id);
    } catch (error) {
      return { items: [], totalQuantity: 0, error: error };
    }
  }, [trainingInstance]);

  const getContentLocales = async () => {
    const account = await getALMAccount();
    const contentLocales = account.contentLocales;
    return contentLocales;
  };

  const jobAidClickHandler = useCallback(
    (supplymentaryLo: PrimeLearningObject) => {
      if (isJobaidContentTypeUrl(supplymentaryLo)) {
        window.open(getJobaidUrl(supplymentaryLo), "_blank");
      } else {
        LaunchPlayer({ trainingId: supplymentaryLo.id });
      }
    },
    []
  );

  const getPlayerLoState = async ({
    loId,
    loInstanceId,
  }: {
    loId: string;
    loInstanceId: string;
  }) => {
    try {
      const userResponse = await getALMUser();
      const userId = userResponse?.user?.id;
      const response = await RestAdapter.ajax({
        url: `${baseApiUrl}/users/${userId}/playerLOState?loId=${loId}&loInstanceId=${loInstanceId}`,
        method: "GET",
      });

      if (typeof response === "string") {
        const parsedResponse = JSON.parse(response);
        setLastPlayingLoResourceId(parsedResponse.lastPlayingLoResourceId);
      }
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    if (!privacyAccepted) return;
    if (trainingInstance?.enrollment && (instanceId || trainingInstance.id)) {
      getPlayerLoState({
        loId: trainingId,
        loInstanceId: trainingInstance.id || instanceId,
      });

      if (trainingInstance.enrollment.state === WAITING) {
        getWaitlistPosition({ enrollmentId: trainingInstance.enrollment.id });
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [trainingInstance, privacyAccepted]);

  useEffect(() => {
    if (!refreshNotes) return;
    if (
      training?.loType === COURSE &&
      trainingInstance?.enrollment &&
      !shouldSkipLOCalls
    ) {
      const buttonElements = document.querySelectorAll('button');
      if (buttonElements) {
        buttonElements.forEach(buttonElement => {
          if (buttonElement.textContent && buttonElement.textContent.includes("Notes") && buttonElement.getAttribute("aria-selected") === "true") {
            buttonElement.click();
          }
        });
      }
      setRefreshNotes(false);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [refreshNotes]);

  const launchPlayerHandler = useCallback(
    async ({
      id,
      moduleId,
      trainingInstanceId,
      isMultienrolled,
      note_id,
      note_position,
      isResetRequired,
    } = {}) => {
      const refreshTrainingandNotes = () => {
        setRefreshTraining((prevState) => !prevState);
        setRefreshNotes(true);
      };
      const loId = id || trainingId;
      let loInstanceId =
        trainingInstanceId || instanceId || trainingInstance.id;

      LaunchPlayer({
        trainingId: loId,
        callBackFn: refreshTrainingandNotes,
        moduleId,
        instanceId: loInstanceId,
        isMultienrolled: isMultienrolled,
        note_id: note_id,
        note_position: note_position,
        isResetRequired: isResetRequired,
      });
    }, // eslint-disable-next-line react-hooks/exhaustive-deps
    [trainingId]
  );

  const alternateLanguages = useMemo(async () => {
    let alternateLanguages = new Set<string>();
    getLocale(trainingInstance, alternateLanguages, locale);
    if (training && training.subLOs) {
      training.subLOs.forEach((subLo) => {
        subLo.instances?.forEach((instance) => {
          getLocale(instance, alternateLanguages, locale);
        });
      });
    }
    if (training && training.subLOs) {
      training.subLOs.forEach((subLo) =>
        subLo.subLOs?.forEach((subLo) => {
          subLo.instances?.forEach((instances) => {
            getLocale(instances, alternateLanguages, locale);
          });
        })
      );
    }
    let alternateLocales: string[] = [];
    var contentLocale = await getContentLocales();

    if (alternateLanguages && alternateLanguages.size) {
      contentLocale?.forEach((contentLocale) => {
        if (alternateLanguages.has(contentLocale.locale)) {
          alternateLocales.push(contentLocale.name);
        }
      });
    }
    return alternateLocales;
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [training]);

  var {
    name = "",
    description = "",
    overview = "",
    richTextOverview = "",
  } = useLocalizedMetaData(training, locale);

  function parseImgTagFromStr(str: string): string {
    const parser = new DOMParser();
    const doc = parser.parseFromString(str, 'text/html');
    // Loop through each img tag and modify the src attribute
    const imgTags = document.querySelectorAll('img');
    if (imgTags) {
      imgTags.forEach(imgTag => {
        let imgSrc = imgTag.getAttribute('src');
        if (imgSrc && !imgSrc.includes('?access_token=') && imgSrc.includes('https://')) {
          imgSrc += `?access_token=${getAccessToken()}`;
          imgTag.setAttribute('src', imgSrc);
        }
      });
      // Serialize the modified HTML back to a string
      const updatedHtmlContent = new XMLSerializer().serializeToString(doc);
      return updatedHtmlContent;
    } else {
      return str;
    }
  }
  richTextOverview = parseImgTagFromStr(richTextOverview);

  const { cardIconUrl, color, bannerUrl, cardBgStyle } = useCardIcon(training);

  // const skills = useTrainingSkills(training);
  // const instanceBadge = useBadge(trainingInstance);

  const updateFileSubmissionUrl = useCallback(
    async (
      fileUrl: string,
      loId: string,
      loInstanceId: string,
      loResourceId: string
    ) => {
      const body = {
        data: {
          id: loResourceId,
          type: "learningObjectResource",
          attributes: {
            submissionUrl: fileUrl,
          },
        },
      };

      try {
        await RestAdapter.ajax({
          url: `${baseApiUrl}/learningObjects/${loId}/loResources/${loResourceId}`,
          method: "PATCH",
          body: JSON.stringify(body),
          headers: headers,
        });
        const params: QueryParams = {};
        params["include"] = "instances.loResources.resources";

        let response = await RestAdapter.ajax({
          url: `${baseApiUrl}/learningObjects/${loId}`,
          method: "GET",
          headers: headers,
          params: params,
        });
        setRefreshTraining((prevState) => !prevState);
        const parsedResponse = JsonApiParse(response);
        const loInstance = parsedResponse.learningObject.instances?.filter(
          (instance) => {
            return instance.id === loInstanceId;
          }
        );
        const loResource = loInstance[0].loResources?.filter((resource) => {
          return resource.id === loResourceId;
        });
        return loResource[0].submissionUrl;
      } catch (e) {
        almAlert(true, GetTranslation("alm.enrollment.error"), AlertType.error);
        console.log(e);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [dispatch]
  );

  const updateRating = useCallback(
    async (rating: number, loInstanceId: string) => {
      const userResponse = await getALMUser();
      const userId = userResponse?.user?.id;
      const body = {
        rating: rating,
      };

      try {
        await RestAdapter.ajax({
          url: `${baseApiUrl}enrollments/${loInstanceId}_${userId}/rate`,
          method: "PATCH",
          body: JSON.stringify(body),
          headers: headers,
        });
        setRefreshTraining((prevState) => !prevState);
      } catch (e) {
        almAlert(true, GetTranslation("alm.enrollment.error"), AlertType.error);
        console.log(e);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [dispatch]
  );
  const updateNote = useCallback(
    async (
      note: PrimeNote,
      updatedText: string,
      loId: string,
      loResourceId: PrimeLearningObjectResource
    ) => {
      const body = {
        data: {
          id: note.id,
          type: "note",
          attributes: {
            text: updatedText,
          },
          relationships: {
            loResource: {
              data: {
                type: "learningObjectResource",
                id: note.loResource.id,
              },
            },
          },
        },
      };

      try {
        await RestAdapter.ajax({
          url: `${baseApiUrl}learningObjects/${loId}/resources/${loResourceId.id}/note/${note.id}`,
          method: "PATCH",
          body: JSON.stringify(body),
          headers: headers,
        });
        setRefreshNotes(true);
      } catch (e) {
        almAlert(true, GetTranslation("alm.enrollment.error"), AlertType.error);
        console.log(e);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [dispatch]
  );
  const deleteNote = useCallback(
    async (noteId: string, loId: string, loResourceId: string) => {
      try {
        await RestAdapter.ajax({
          url: `${baseApiUrl}learningObjects/${loId}/resources/${loResourceId}/note/${noteId}`,
          method: "DELETE",
          headers: headers,
        });
        setRefreshNotes(true);
      } catch (e) {
        almAlert(true, GetTranslation("alm.enrollment.error"), AlertType.error);
        console.log(e);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [dispatch]
  );
  const downloadNotes = useCallback(
    async (loId: string, loInstanceId: string, courseName: string) => {
      const url = `${getALMConfig().primeApiURL
        }learningObjects/${loId}/instances/${loInstanceId}/note/download`;
      try {
        const headers = {
          Authorization: `oauth ${getAccessToken()}`,
        };

        const response = await fetch(url, { headers });
        const blob = await response.blob();
        const blobUrl = URL.createObjectURL(blob);

        const link = document.createElement("a");
        link.href = blobUrl;
        link.style.display = "none";
        link.setAttribute("download", courseName);

        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      } catch (error) {
        console.error("Error downloading file:", error);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [dispatch]
  );

  const sendNotesOnMail = useCallback(
    async (loId: string, loInstanceId: string) => {
      try {
        await RestAdapter.ajax({
          url: `${baseApiUrl}learningObjects/${loId}/instances/${loInstanceId}/note/email`,
          method: "POST",
          headers: headers,
        });

        almAlert(
          true,
          formatMessage({
            id: "alm.text.notesSentMessage",
            defaultMessage: "Notes have been mailed to your email account",
          }),
          AlertType.success
        );
      } catch (e) {
        almAlert(true, GetTranslation("alm.enrollment.error"), AlertType.error);
        console.log(e);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [dispatch]
  );

  const getAllNotes = useCallback(
    async (trainingId: string, loInstanceId?: string) => {
    try {
      let url = "";
      if (loInstanceId) {
        url = `${baseApiUrl}learningObjects/${trainingId}/instances/${loInstanceId}/note`;
      } else {
        url = `${baseApiUrl}learningObjects/${trainingId}/note`;
      }
      let response = await RestAdapter.ajax({
        url: url,
        method: "GET",
        headers: headers,
      });
      const parsedResponse = JsonApiParse(response);
      return parsedResponse.noteList;
    } catch (e) {
      console.log(e);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dispatch, instanceId, training]);

  const updateCertificationProofUrl = useCallback(
    async (fileUrl: string, loId: string, loInstanceId: string) => {
      const userResponse = await getALMUser();
      const userId = userResponse?.user?.id;
      const body = {
        data: {
          id: loInstanceId + "_" + userId,
          type: "learningObjectInstanceEnrollment",
          attributes: {
            url: fileUrl,
          },
        },
      };

      try {
        await RestAdapter.ajax({
          url: `${baseApiUrl}/enrollments/${loInstanceId + "_" + userId}`,
          method: "PATCH",
          body: JSON.stringify(body),
          headers: headers,
        });
        const params: QueryParams = {};
        params["include"] = "enrollment.loInstance";

        let response = await RestAdapter.ajax({
          url: `${baseApiUrl}/learningObjects/${loId}`,
          method: "GET",
          headers: headers,
          params: params,
        });
        const parsedResponse = JsonApiParse(response);
        return parsedResponse.learningObject.enrollment.url || "";
      } catch (e) {
        almAlert(true, GetTranslation("alm.enrollment.error"), AlertType.error);
        console.log(e);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [dispatch]
  );

  const updateBookMark = useCallback(
    async (isBookMarked: boolean, loId: string) => {
      try {
        await RestAdapter.ajax({
          url: `${baseApiUrl}/learningObjects/${loId}/bookmark`,
          method: isBookMarked ? "POST" : "DELETE",
        });
      } catch (e) {
        console.log(e);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [dispatch]
  );

  // Removing current LO data from parent path stack
  useEffect(() => {
    if (!privacyAccepted) return;
    if (trainingInstance?.id) {
      const id = training?.id || trainingId;
      popFromParentPathStack(id);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [trainingInstance, privacyAccepted]);

  return {
    name,
    description,
    overview,
    richTextOverview,
    cardIconUrl,
    color,
    bannerUrl,
    cardBgStyle,
    // skills,
    training,
    trainingInstance,
    isLoading,
    // instanceBadge,
    instanceSummary,
    isPreviewEnabled,
    enrollmentHandler,
    launchPlayerHandler,
    updateEnrollmentHandler,
    unEnrollmentHandler,
    jobAidClickHandler,
    addToCartHandler,
    errorCode,
    updateRating,
    updateFileSubmissionUrl,
    updateCertificationProofUrl,
    alternateLanguages,
    updateBookMark,
    trainingOverviewAttributes,
    flexLpEnrollHandler,
    getAllNotes,
    updateNote,
    deleteNote,
    downloadNotes,
    sendNotesOnMail,
    lastPlayingLoResourceId,
    waitlistPosition,
    sendInstanceId,
    selectedInstanceInfo,
    setInstancesForFlexLPOnLoad,
    setSelectedLoList,
    selectedLoList,
  };
  //date create, published, duration
};
