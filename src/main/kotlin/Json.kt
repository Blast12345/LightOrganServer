import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.module.kotlin.jsonMapper
import tools.jackson.module.kotlin.kotlinModule

val JsonMapper = jsonMapper {
    addModule(kotlinModule())
    propertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
}