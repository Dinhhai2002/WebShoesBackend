module.exports = function (plop) {
    plop.setGenerator("spring-boot-crud", {
      description: "Generate DAO, Service, and Controller for a Spring Boot entity",
      prompts: [
        {
          type: "input",
          name: "entity",
          message: "Enter the entity name (e.g., Category):",
        },
      ],
      actions: [
        {
          type: "add",
          path: "src/main/java/com/shoes/webshoes/dao/{{entity}}Dao.java",
          templateFile: "plop-templates/dao/dao.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/shoes/webshoes/dao/impl/{{entity}}DaoImpl.java",
          templateFile: "plop-templates/dao/daoImpl.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/shoes/webshoes/service/{{entity}}Service.java",
          templateFile: "plop-templates/service/service.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/shoes/webshoes/service/impl/{{entity}}ServiceImpl.java",
          templateFile: "plop-templates/service/serviceImpl.hbs",
        },
        {
          type: "add",
          path: "src/main/java/com/shoes/webshoes/controller/{{entity}}Controller.java",
          templateFile: "plop-templates/controller/controller.hbs",
        },
      ],
    });
    plop.setHelper("lowerCase", function (text) {
        return text.charAt(0).toLowerCase() + text.slice(1);
      });
    plop.setHelper("upperCase", function (text) {
        return text.toUpperCase();
    });
  };
  