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
/* eslint-disable no-script-url */
/* eslint-disable jsx-a11y/anchor-is-valid */

import styles from "./PrimeTrainingPageExtraDetailsJobAids.module.css";
import {
  PrimeLearningObject,
  PrimeResource,
} from "../../../models/PrimeModels";
import { PrimeAlertDialog } from "../../Community/PrimeAlertDialog";
import { GetTranslation } from "../../../utils/translationService";
import { useJobAids } from "../../../hooks/useJobAids";

const PrimeTrainingPageExtraJobAid: React.FC<{
  resource: PrimeResource;
  training: PrimeLearningObject;
  enrollmentHandler: Function;
  unEnrollmentHandler: Function;
  jobAidClickHandler: Function;
}> = ({
  resource,
  training,
  enrollmentHandler,
  unEnrollmentHandler,
  jobAidClickHandler,
}) => {
  const {
    nameClickHandler,
    showAlert,
  } = useJobAids(
    training,
    enrollmentHandler,
    unEnrollmentHandler,
    jobAidClickHandler
  );

  const jobAidClick = (event: React.MouseEvent<HTMLAnchorElement, MouseEvent>) => {
    event.preventDefault();
    nameClickHandler();
  };

  return (
    <div>
      <a
        className={styles.name}
        onClick={jobAidClick}
        role="button"
        tabIndex={0}
        href="#"
      >
        {resource.name}
      </a>
      {showAlert && (
        <PrimeAlertDialog
          variant="warning"
          title={GetTranslation("alm.overview.job.aid.not.in.list", true)}
          primaryActionLabel="Ok"
          classes={styles.warningDialog}
          show={true}
        ></PrimeAlertDialog>
      )}
    </div>
  );
};

export default PrimeTrainingPageExtraJobAid;
