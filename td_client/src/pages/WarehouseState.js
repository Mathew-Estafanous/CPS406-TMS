import '../App.css';
import WarehouseStateMenu from "../components/WarehouseStateMenu/WarehouseStateMenu";

/**
 * The Warehouse State page.
 * @return {JSX.Element} UI for the Warehouse State page.
 * @constructor
 */
function WarehouseState() {
    return (
        <div className="App">
            <div className="Background-Image">
                <WarehouseStateMenu/>
            </div>
        </div>
    );
}

export default WarehouseState;
