import utils.JobConfig

folder('Demo')
def springBootPipelineJob = multibranchPipelineJob('Demo/Spring Boot App Multibranch Pipeline')
JobConfig.basicPipeline(
        springBootPipelineJob,
        repo = "spring-boot-gradle-boilerplate",
        includeBranches = "master",
        ignoreOnPush = false,
        buildPR = true,
        jenkinsfilePath = "Jenkinsfile"
)