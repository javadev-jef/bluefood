/**
 * SHOW FOOD ANIMATION
 */
function renderAnimationFoods()
{
    const media = window.matchMedia("(max-width: 768px)");
    const ulFoods = document.querySelector("ul.foods");
    for(let i = 0; i< 11; i++)
    {
        const li = document.createElement("li");

        const random = (min, max) => Math.random() * (max - min) + min;

        const size = Math.floor(random(10, 120));
        const position = random(1, 99);
        const delay = random(5, 0.1);
        const duration = random(24, 12);

        li.style.width = `${size}px`;
        li.style.height = `${size}px`;
        li.style.bottom = `-${size}px`;

        if(media.matches && i < 6)
        {
            li.style.backgroundImage = `url('/images/food/food-gray-login${i+1}.png')`;
        }
        else{
            // PRODUCTION ONLY
            li.style.backgroundImage = `url('/images/food/food-white-login${i+1}.png')`;
        }

        li.style.backgroundSize = "cover";
        li.style.setProperty("opacity", "0.5", "important");

        li.style.left = `${position}%`;

        li.style.animationDelay = `${delay}s`;
        li.style.animationDuration = `${duration}s`;

        li.style.animationTimingFunction = `cubic-bazier(${Math.random()}, ${Math.random()}, ${Math.random()}, ${Math.random()})`;

        ulFoods.appendChild(li);
    }
}