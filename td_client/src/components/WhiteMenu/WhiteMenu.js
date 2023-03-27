import './WhiteMenu.css';
import CheckInMenu from "../CheckInMenu/CheckInMenu"
import WaitingAreaMenu from "../WaitingAreaMenu/WaitingAreaMenu"
import DockingAreaMenu from "../DockingAreaMenu/DockingAreaMenu"
import AdminLoginMenu from "../AdminLoginMenu/AdminLoginMenu";
import AdminPortalMenu from "../AdminPortalMenu/AdminPortalMenu";

/**
 * UI Menu for various pages.
 * @param {String} pageType Determines which menu to display.
 * @return {JSX.Element} The menu to be displayed.
 */
function WhiteMenu({pageType}) {
    return (
        <div className="WhiteMenu">
            {(() => {
                switch (pageType) {
                    case "CheckIn":
                        return <CheckInMenu/>
                    case "WaitingArea":
                        return <WaitingAreaMenu/>
                    case "DockingArea":
                        return <DockingAreaMenu/>
                    case "AdminLogin":
                        return <AdminLoginMenu/>
                    case "AdminPortal":
                        return <AdminPortalMenu/>
                    default:
                        return <CheckInMenu/>
                }
            })()}
        </div>
    );
}

export default WhiteMenu;