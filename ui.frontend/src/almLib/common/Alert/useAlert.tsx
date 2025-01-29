import { useEffect, useState } from "react";
import { render } from "react-dom";
import { AlertDialog, AlertType } from "./AlertDialog";
import { useIntl } from "react-intl";

let alertMesssage: string = "Unknown Error";
let alertType: "success" | "error";

const useAlert = (): [
  (show: boolean, messsage: string, type: AlertType, closeAlert?: boolean, timeOut?: number) => void
] => {
  const [showAlert, setShowAlert] = useState<boolean>(false);
  const [isClosable, setIsClosable] = useState<boolean>(true);
  const { locale } = useIntl();

  const almAlert = (
      show: boolean,
      messsage: string,
      type: AlertType,
      showAlertClose?: boolean,
      timeOut?: number,
  ) => {
    alertType = type;
    alertMesssage = messsage;
    setShowAlert(show);

    if (showAlertClose !== undefined) {
      setIsClosable(showAlertClose);
    }

    if (timeOut) {
      setTimeout(() => {
        setShowAlert(false);
      }, timeOut);
    }
  };

  const alertTemplate = () => (
      <AlertDialog
          type={alertType}
          show={showAlert}
          message={alertMesssage}
          closeAlert={isClosable}
          setShowAlert={setShowAlert}
          locale={locale}
      />
  );

  useEffect(() => {
    render(alertTemplate(), document.getElementById("alertDialog"));
  }, [showAlert, isClosable]); // Added `isClosable` to dependencies

  return [almAlert];
};

export { useAlert };
