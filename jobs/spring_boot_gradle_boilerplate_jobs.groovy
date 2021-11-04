import utils.JobConfig

folder('Demo')

def springBootBasicJob = job('Demo/Spring Boot App Basic Job')
JobConfig.basicJob(
        springBootBasicJob,
        repo = "spring-boot-gradle-boilerplate",
        includeBranches = "main",
        ignoreOnPush = false,
        buildPR = true
)

def springBootPipelineJob = multibranchPipelineJob('Demo/Spring Boot App Multibranch Pipeline')
JobConfig.basicPipeline(
        springBootPipelineJob,
        repo = "spring-boot-gradle-boilerplate",
        includeBranches = "develop",
        ignoreOnPush = false,
        buildPR = true,
        jenkinsfilePath = "Jenkinsfile"
)