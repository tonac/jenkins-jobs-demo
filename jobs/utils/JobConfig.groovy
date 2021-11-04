package utils

class JobConfig {

    static def basicJob(job, repo, includeBranches = "master *.x-maintenance PR-*", ignoreOnPush = false,
                        buildPR = true, cronTrigger = 'H 23 * * *') {
        job.with {
            scm {
                git(repo) { node ->
                    // is hudson.plugins.git.GitSCM
                    node / gitConfigName('tonac')
                    node / gitConfigEmail('antonio.sostar56@gmail.com')
                }
            }

            steps {
                gradle {
                    tasks("clean build")
                    useWrapper(true)
                    makeExecutable(true)
                }

                shell("echo Hello")
            }

            triggers {
                cron(cronTrigger)
            }
        }
    }

    static def basicPipeline(job, repo, includeBranches = "master *.x-maintenance PR-*", ignoreOnPush = false,
                             buildPR = true, jenkinsfilePath = "Jenkinsfile", cronTrigger = 'H 23 * * *') {
        job.with {
            cpsScm {
                scm {
                    git {
                        remote {
                            github("tonac/$repo")
                        }
                    }
                }
            }
        }
    }
}