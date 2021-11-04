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
//            branchSources {
//                git {
//                    id('123456789') // IMPORTANT: use a constant and unique identifier
//                    remote("$githubAddress/tonac/$repo")
//                    includes(includeBranches)
//                }
//            }

            configure {
                it / factory(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory') {
                    owner(class: 'org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject', reference: '../..')
                    scriptPath(jenkinsfilePath)
                }

                it / sources(class: 'jenkins.branch.MultiBranchProject$BranchSourceList') / 'data' / "jenkins.branch.BranchSource" {
                    source(class: "org.jenkinsci.plugins.github_branch_source.GitHubSCMSource") {
                        id('GitHubSource')
                        repoOwner('tonac')
                        repository(repo)
                        ignoreOnPushNotifications(true)

                        traits {
                            'org.jenkinsci.plugins.github__branch__source.BranchDiscoveryTrait' {
                                strategyId('1')
                            }
                            if (buildPR) {
                                'org.jenkinsci.plugins.github__branch__source.OriginPullRequestDiscoveryTrait' {
                                    strategyId('1')
                                }
                            }
                            'jenkins.scm.impl.trait.WildcardSCMHeadFilterTrait' {
                                includes(includeBranches)
                                excludes()
                            }
                            'org.jenkinsci.plugins.github__branch__source.SSHCheckoutTrait' {
                                credentialsId(JENKINS_CREDENTIALS_FOR_GITHUB)
                            }
                            'jenkins.plugins.git.traits.CloneOptionTrait' {
                                extension(class: "hudson.plugins.git.extensions.impl.CloneOption") {
                                    shallow("false")
                                    noTags("false")
                                    depth("0")
                                    honorRefspec("false")
                                }
                            }
                            if (ignoreOnPush) {
                                'jenkins.plugins.git.traits.IgnoreOnPushNotificationTrait'()
                            }
                        }
                    }
                    strategy(class: "jenkins.branch.DefaultBranchPropertyStrategy") {
                        properties(class: "empty-list")
                    }
                }
            }
        }
    }
}