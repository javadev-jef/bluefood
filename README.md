# bluefood
 Aplicação web que disponibiliza cardápios, preços, e forma de pagamento, simplificando o pedido de comida.

# Pré requisitos
- Git
- Java 11 ou superior
- Mysql

# Preview
![Telas Pages](https://github.com/javadev-jef/bluefood/blob/master/bluefood-pages.gif)

# Importando o projeto
Ao importar o projeto é necessário adicionar a JVM um argumento (vmArgs) para que ao iniciar o projeto, seja carregado o profile de desenvolvimento (DEV).

**VS Code**
```
"vmArgs": "-Dspring.profiles.active=dev"
```
**Eclipse**
```
VM arguments: -Dspring.profiles.active=dev
```
# Incluindo as imagens
As imagens de exemplo referente as comidas e restaurantes criados automaticamente, devem ser armazenadas fora da pasta do projeto.
<br>**Ex:** pasta example_images. <br>**application-dev.properties**
```
bluefood.files.logotipo=D:/temp/sbfood-files/logotipos
bluefood.files.categoria=D:/temp/sbfood-files/categoria-comidas
bluefood.files.categoria.capas=D:/temp/sbfood-files/categoria-comidas/capas
bluefood.files.comida=D:/temp/sbfood-files/comidas
bluefood.files.comida.capas=D:/temp/sbfood-files/comidas/capas
bluefood.files.restaurante=D:/temp/sbfood-files/categoria-restaurante
bluefood.files.banners=D:/temp/sbfood-files/banner-restaurante
```

# API Pagar.Me
É necessário gerar uma chave de acesso para poder realizar as simulações de pagamento. Para isso você precisará acessar o site do [Pagar.Me](https://dashboard.pagar.me/#/signup) e criar uma conta para que consiga gerar a chave. Para mais detalhes sobre a documentação clique [aqui](https://docs.pagar.me/reference). Após estar com a chave em mãos, basta incluilá em **application.properties**.
```
bluefood.pagarme.api_key=YOUR_PAGAR_ME_KEY
```
