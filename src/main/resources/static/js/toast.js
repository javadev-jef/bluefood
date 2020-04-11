/**
 * SHOW TOAST 
 * @param {String} idElementMessageToast 
 */
function showToastPage(idElementMessageToast)
{
    // INPUT HIDDEN WITH BOOLEAN VALUE
    let validationError = document.getElementById("validationError");

    if(validationError != null)
    {
        // CONVERT STRING TO REAL BOOLEAN VALUE
        var hasErrors = JSON.parse(validationError.value);

        // GET TOAST MESSAGE
        let msg = document.getElementById(idElementMessageToast).innerText;

        if(hasErrors)
        {
            buildToast(msg, hasErrors);
        }
        else{
            buildToast(msg, hasErrors);
        }
    }
}

/**
 * BUILD AND SHOW TOAST
 * @param {String} msg 
 * @param {Boolean} error 
 */
function buildToast(msg, error)
{
    let toast = new Toastify();
    toast.options.duration = 5000;
    toast.options.text = msg;
    toast.options.backgroundColor = error ? "linear-gradient(to right, #EB3349 0%, #F45C43 51%, #EB3349 100%)" : "linear-gradient(to right, #76b852 0%, #8DC26F 51%, #76b852 100%)";
    toast.options.gravity = "bottom";
    toast.options.stopOnFocus = true;
    toast.options.close = true;
    toast.showToast();
}