import utils.JobConfig

folder('Demo')

def springBootBasicJob = job('Demo/Spring Boot App Basic Job')
JobConfig.basicJob(
        springBootBasicJob,
        repo = 'tonac/spring-boot-gradle-boilerplate.git',
        includeBranches = "main",
        ignoreOnPush = false,
        buildPR = true
)

def springBootPipelineJob = multibranchPipelineJob('Demo/Spring Boot App Multibranch Pipeline')
JobConfig.basicPipeline(
        springBootPipelineJob,
        repo = "tonac/spring-boot-gradle-boilerplate.git",
        includeBranches = "develop",
        ignoreOnPush = false,
        buildPR = true,
        jenkinsfilePath = "Jenkinsfile"
)