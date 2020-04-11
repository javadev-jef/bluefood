/**
 * - SUBMIT PARENT ELEMENT (FORM) BY CHILD ELEMENT
 * @param {HTMLInputElement} element CHILD ELEMENT OF THE FORM
 */
function submitForm(element)
{
    let form = element.parentElement;
    let sType = form.getElementsByClassName("searchType")[0];

    if(sType instanceof HTMLInputElement && sType != null)
    {
        sType.value = "TEXTO";
    }

    if(form instanceof HTMLFormElement)
    {
        form.submit();
    }
}

/**
 * - SHOW ITEMS BY SELECTED CATEGORY
 * @param {BigInteger} categoriaId 
 * @param {BigInteger} restauranteId 
 */
function filterCategoriaRest(categoriaId, restauranteId)
{
    const form = document.getElementById("formFilterCategoria");

    if(categoriaId != null)
    {
        form.getElementsByClassName("categoriaId")[0].value = categoriaId;
    }
    else if(categoriaId == null && restauranteId != null)
    {
        form.getElementsByClassName("categoriaId")[0].value = null;
    }

    form.submit();
}

/**
 * - MODAL SHOW WHEN ITEM IS SELECTED
 * @param {String} itemCardapioId 
 */
function openModalProduct(itemCardapioId)
{
    const form = document.getElementById("formItensCardapio");
    form.getElementsByClassName("itemCardapioId")[0].value = itemCardapioId;

    form.submit();
}

/**
 * - DEFAULT VALUE -> NULL / OR
 * TRUE IF PEDIDO-LIST SHOULD BE OPEN
 * @param {Boolean} openPedidoTab 
 */
function onloadRestauranteHome(openPedidoTab)
{
    if(openPedidoTab != null && JSON.parse(openPedidoTab))
    {
        document.getElementById("tab-pedidos").click();
    }
    else
    {
        let media = window.matchMedia("(min-width: 768px)");
        checkWidthWindow(media);
        media.addListener(checkWidthWindow);
    }
}

/**
 * - ONLY WINDOW RESTAURANTE HOME
 * @param {MediaQueryList} media 
 */
function checkWidthWindow(media)
{
    if(!media.matches)
    {
        // OPEN DEFAULT TAB
        document.getElementById("tab-default-open").click();
    }
    else
    {
        document.getElementById("painel-adm").style.display = "block";
        document.getElementById("pedidos").style.display = "block";
    }
}

/**
 * - SEARCH RESTAURANT BY EVENT KEY
 * @param {KeyboardEvent} event 
 * @param {String} idForm 
 */
function searchRestKey(event, idForm)
{
    if(event.keyCode === 13)
    { 
        searchRest(null, null, idForm);
    }
}

/**
 * - SEARCH RESTAURANT
 * @param {BigInteger} idCategoria
 * @param {String} idForm
 */
function searchRest(element, idCategoria, idForm)
{
    let form = document.getElementById(idForm);
    let sType = form.getElementsByClassName("searchType")[0];

    if(idCategoria == null)
    {
        sType.value = "TEXTO";
    }
    else
    {
        sType.value = "CATEGORIA";
        form.getElementsByClassName("idCategoria")[0].value = idCategoria;
        
        let textoCategoria = element.getElementsByClassName("categoria-text")[0].innerText;
        form.getElementsByClassName("descCategoria")[0].value = textoCategoria;

    }

    form.submit();
}

/**
 * - INCREASE/DECREASE INPUT VALUE
 * @param {String} funcao ('SUM' ou 'SUB')
 * @param {String} inputId
 */
function calcValueByType(type, inputId)
{
    const input = document.getElementById(inputId);

    if(input instanceof HTMLInputElement)
    {
        let qtdAtual = parseInt(input.value);

        if(type ==="SUM")
        {
            input.value =  (qtdAtual + 1).toString();
        }
        else if(type === "SUB")
        {
            qtdAtual > 1 ? input.value = String(qtdAtual - 1) : String(0);
        }
        else
        {
            alert("Valores suportados: 'SUB' ou 'SUM'");
        } 
    } 
}

/**
 * - HIDE MODAL ITEM
 */
function hideModal()
{
    const modal = document.getElementById("modal");
    modal.style.display = "none";
}

/**
 * - OPEN CONTENT-TAB MOBILE
 * @param {String} contentName 
 * @param {HTMLInputElement} element 
 */
function openContentTab(idContentTab, element)
{
    // HIDE ALL ELEMENTS WITH class="tabcontent" BY DEFAUL */

    let i, tabscontent, tablinks, tabSelected;

    tabSelected =  document.getElementById(idContentTab);

    if(idContentTab === "novo-item")
    {
        let form;

        form = tabSelected.getElementsByTagName("form")[0];
        if(form != undefined)
        {
            form.reset();
        }
    }

    tabscontent = document.getElementsByClassName("tabcontent");
    for(i=0; i<tabscontent.length; i++)
    {
        tabscontent[i].style.display = "none";
    }

    // Alter opacity of all tab-buttons
    tablinks = document.getElementsByClassName("tab-button");
    for(i=0; i<tablinks.length; i++)
    {
        tablinks[i].style.opacity = "1";
    }

    element.style.opacity = "0.5";

    // Show the specific tab content
    tabSelected.style.display = "block";
}

/**
 * - SELECT DEFAULT TAB OR SIGN UP TAB (IN CASE OF ERROR BY SUBMIT)
 * - MODE MOBILE ONLY
 * @param {String} idItem 
 */
function onloadRestauranteComida(idItem)
{
    loadInputFiles();
    const validationError = document.getElementById("validationError");
    const hasError = validationError != null ? JSON.parse(validationError.value) : null;

    if(idItem != null || hasError)
    {
        document.getElementById("novo-item-tab").click();
        return;
    }

    // OPEN DEFAULT TAB
    document.getElementById("tab-default-open").click();
}

/**
 * - LOAD ALL INPUTS-FILE AND ADD A CHANGE LISTENER
 */
function loadInputFiles()
{
    // GET INPUT-FILE ELEMENTS
    const inputsFile = document.querySelectorAll("input[type=file]");

    // ITERATE OVER EACH INPUT-FILE
    for(const inputFile of inputsFile)
    {
        inputFile.addEventListener('change', function()
        {
            // RETURNS ONLY AN INPUT-LABEL BINDED AN INPUT-FILE
            const labelInput = this.labels[0];

            // RETURNS A FILE-LIST WITH SELECTED FILES IN INPUT-FILE
            const currentFiles = this.files;

            if(currentFiles.length > 1)
            {
                labelInput.innerText = currentFiles.length + " arquivos selecionados";
            }
            else if(currentFiles.length == 0)
            {
                labelInput.innerText = "Clique aqui para selecionar";
            }
            else
            {
                labelInput.innerText = currentFiles[0].name;
            }
        });
    }
}

/**
 * - GET DEFAULT OPTION-FILTER AND SHOW ONLY YOUR INPUT
 */
function onloadRelatorioPage()
{
    let selectedOption = document.getElementById("OptionFilter");
    changeFilterRelatorio(selectedOption);
}

/**
 * - RECEIVE OPTION-FILTER AND SHOW YOUT INPUT
 * @param {HTMLSelectElement} element 
 */
function changeFilterRelatorio(element)
{
    let form = element.parentElement;
    let selectedOption = form.getElementsByTagName("select")[0].value;
    let inputPedidoId = form.getElementsByClassName("numero-filter");
    let inputsDate = form.getElementsByClassName("periodo-filter");
    
    if(selectedOption === "IDPEDIDO" || selectedOption === "ITEMID")
    {
        alterVisibilityFilter(inputsDate, inputPedidoId, false);

        // Fixed bug clearDate
        let clearBtnDatePickers = document.getElementsByClassName("clear-btn");
        clearBtnDatePickers[0].click();
        clearBtnDatePickers[1].click();
    }
    else if(selectedOption === "PERIODO")
    {
        alterVisibilityFilter(inputPedidoId, inputsDate, false);
    }
    else if(selectedOption == "PERIODO_AND_ITEMID")
    {
        alterVisibilityFilter(inputPedidoId, inputsDate, true);
    }
}

/**
 * - CHANGE VISIBILITY OF INPUT
 * @param {HTMLInputElement} inputHide 
 * @param {HTMLInputElement} inputShow 
 * @param {Boolean} showAll 
 */
function alterVisibilityFilter(inputHide, inputShow, showAll)
{
    for(var i=0; i < inputHide.length; i++)
    {
        inputHide[i].value = showAll ? inputHide[i].value : "";
        inputHide[i].style.display = showAll ? "initial" : "none";
    }

    for(var i=0; i < inputShow.length; i++)
    {
        inputShow[i].style.display = "initial";
    }
}

/**
 * SHOW CONFIRM DIALOG
 */
function confirmChangesUser()
{
    const msg = "Tem certeza que deseja realizar as alterações? Será necessário se autenticar novamente";
    return confirm(msg);
}

/**
 * - ENABLE/DISABLE PASSWORD INPUT
 * @param {Boolean} checkbox 
 * @param {String} idInputPwd 
 */
function enableInputSenha(checkbox, idInputPwd)
{
    const isChecked = checkbox.checked;
    const inputPwd = document.getElementById(idInputPwd);
    const inputHiddenPwd = document.getElementById(idInputPwd+"-hidden");
    const spanInputPwd = document.getElementById("span-"+idInputPwd);

    inputPwd.disabled = !isChecked;

    if(isChecked)
    {
        inputHiddenPwd.setAttribute("name", "");
        spanInputPwd != null ? spanInputPwd.style.display = "block" : null;
    }
    else{
        spanInputPwd != null ? spanInputPwd.style.display = "none" : null;
        inputHiddenPwd.setAttribute("name", "senha");
        inputPwd.value = "";
    }
}

/**
 * DISABLED BUTTON FORM
 * @param {String} buttonId 
 */
function disableButtonOnSubmit(buttonId)
{
    const button = document.getElementById(buttonId);
    button.value = "Processando...";
    button.disabled = true;
}