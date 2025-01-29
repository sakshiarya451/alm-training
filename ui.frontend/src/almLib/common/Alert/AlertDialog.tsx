import React, {useEffect, useState} from "react";
import styles from "./AlertDialog.module.css";
import Alert from "@spectrum-icons/workflow/Alert";
import CheckmarkCircleOutline from "@spectrum-icons/workflow/CheckmarkCircleOutline";
import { lightTheme, Provider } from "@adobe/react-spectrum";
import {Alert_CLOSE_Icon} from "../../utils/inline_svg";

export enum AlertType {
  success = "success",
  error = "error",
}

export const renderAlert = (type: "success" | "error") => {
  switch (AlertType[type]) {
    case AlertType.success:
      return (
        <CheckmarkCircleOutline
          UNSAFE_className={`${styles.alertIcon} ${styles.success}`}
        />
      );
    case AlertType.error:
      return <Alert UNSAFE_className={`${styles.alertIcon} ${styles.error}`} />;
  }
};
const AlertDialog: React.FC<{
  type: "success" | "error";
  show: boolean;
  message: string;
  closeAlert: boolean;
  setShowAlert: (show: boolean) => void;
  locale: string;
}> = ({ type, show, message, closeAlert, setShowAlert, locale }) => {

  const handleAlertClose = () => {
    setShowAlert(false);
  };

  return (
    <Provider 
      theme={lightTheme} 
      colorScheme={"light"}
      locale={locale}
      >
      {show && (
        <div className={styles.alertbackdrop}>
          {closeAlert && (
              <div className={styles.alertpopupclose}>
                <span onClick={handleAlertClose}>{Alert_CLOSE_Icon()}</span>
              </div>
          )}
          <div className={`${styles.alert} ${styles.dialog}`}>
            <div className={styles.alertIconType}>{renderAlert(type)}</div>
            <div
                className={styles.alertMessage}
                role="alert"
                aria-atomic="true"
                aria-live="assertive"
                dangerouslySetInnerHTML={{__html: message}}
            ></div>
          </div>
        </div>
      )}
    </Provider>
  );
};
export {AlertDialog};
