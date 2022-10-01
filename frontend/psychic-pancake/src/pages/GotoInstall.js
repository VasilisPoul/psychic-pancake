import { Link } from "react-router-dom"

export default function GoToInstall() {
    return (
        <>
            <div>
                <span>
                    Application not installed.
                </span>
            </div>
            <div>
                <span>
                    To install it click
                    <Link to='/install'>here</Link>
                </span>
            </div>
        </>
    )
}