window.addEventListener("load", (event)=>
{
    // ALL MASKS
    const masks = {
        cpf(value)
        {
            return value
                .replace(/\D/g, "")
                .replace(/(\d{3})(\d)/, "$1.$2")
                .replace(/(\d{3})(\d)/, "$1.$2")
                .replace(/(\d{3})(\d{1,2})/, "$1-$2")
                .replace(/(-\d{2})\d+?$/, "$1");
        },
    
        cnpj(value)
        {
            return value
                .replace(/\D/g, "")
                .replace(/(\d{2})(\d)/, "$1.$2")
                .replace(/(\d{3})(\d)/, "$1.$2")
                .replace(/(\d{3})(\d)/, "$1/$2")
                .replace(/(\d{4})(\d)/, "$1-$2")
                .replace(/(-\d{2})\d+?$/, "$1");
        },

        integer(value)
        {
            return value
                .replace(/\D/g, "");
        },
    
        telefone(value)
        {
            return value
                .replace(/\D/g, "")
                .replace(/(\d{2})(\d)/, "($1) $2")
                .replace(/(\d{4})(\d)/, "$1-$2")
                .replace(/(\d{4})-(\d)(\d{4})/, "$1$2-$3")
                .replace(/(-\d{4,5})\d+?$/, "$1");
        },
    
        cep(value)
        {
            return value
                .replace(/\D/g, "")
                .replace(/(\d{5})(\d)/, "$1-$2")
                .replace(/(-\d{3})\d+?$/, "$1");
        }
    };

    // GET ALL INPUT ELEMENTS ON THE PAGE
    document.querySelectorAll("input").forEach((input) =>
    {
        const field = input.dataset.js;  
        masksListener("input", field, input);
        masksListener("focus", field, input);
    });

    /**
     * ADD A LISTENER TO THE INPUT ELEMENT
     * @param {String} event EVENT TYPE (focus, input, etc..)
     * @param {String} field CPF, TELEFONE, ETC..
     * @param {HTMLInputElement} input INPUT ELEMENT
     */
    function masksListener(event, field, input)
    {
        if(field != undefined)
        {
            input.addEventListener(event, (e)=>{
                e.target.value = masks[field](e.target.value);
            }, false);
        }
    }    
;})

// CLEAR ALL MASKS
function clearMasks()
{
    document.querySelectorAll("input[type=text]").forEach((input) => 
    {
        if(input instanceof HTMLInputElement && input.dataset.js != null)
        {
            input.value = input.value.replace(/\D/g, "");
        }  
    });
}