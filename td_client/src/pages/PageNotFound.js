import notFoundPic from "../NoPageImg.png"

/**
 * 404 Page
 * @return {JSX.Element} UI for 404 page.
 */
function PageNotFound() {
    return (
        <div className="App">
            <div className="Background-Image">
                <img className={"notFoundImg"} src={notFoundPic} alt={"404"}/>
            </div>
        </div>
    )
}

export default PageNotFound;