
$(function(){
    $(".banners-restaurante .banners-restaurante-flex").slick(
        {
            centerMode: false,
            variableWidth: true,
            slidesToShow:3,
            swipeToSlide: true,
            speed:1000,
            autoplay:true,
            nextArrow: $(".btn-next-restaurante-slick"),
            prevArrow:$(".btn-prev-restaurante-slick"),
            responsive:[
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 1
                    }
                }
            ]
        });

        $(".categorias-restaurante .categorias-restaurante-flex").slick(
        {
            centerMode: false,
            slidesToShow:10,
            swipeToSlide: true,
            speed:1000,
            infinite: true,
            variableWidth: true,
            autoplay:true,
            nextArrow: $(".btn-next-comida-slick"),
            prevArrow: $(".btn-prev-comida-slick"),
            responsive:[
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow:4,
                        arrows:false
                    }
                }
            ]
        });
});