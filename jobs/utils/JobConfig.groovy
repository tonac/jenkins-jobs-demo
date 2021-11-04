package utils

class JobConfig {
    static final def githubAddress = 'https://github.com'

    static def basicJob(job, repo, includeBranches = "master *.x-maintenance PR-*", ignoreOnPush = false,
                             buildPR = true, cronTrigger = 'H 23 * * *') {
        job.with {
            scm {
                git("$githubAddress/$repo") {  node ->
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
            branchSources {
                github {
                    repository(repo)
                    includes(includeBranches)
                    buildOriginPRHead(buildPR)
                }
            }

            configure {
                it / factory(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory') {
                    owner(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject', reference: '../..')
                    scriptPath(jenkinsfilePath)
                }
            }

            triggers {
                cron(cronTrigger)
            }
        }
    }
}