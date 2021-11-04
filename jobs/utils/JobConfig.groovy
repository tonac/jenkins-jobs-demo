package utils

class JobConfig {

    static def basicJob(job, repo, includeBranches = "master *.x-maintenance PR-*", ignoreOnPush = false,
                             buildPR = true, cronTrigger = 'H 23 * * *') {
        job.with {
            configure {
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

            steps {
                gradle {
                    tasks("clean build")
                    useWrapper(true)
                    makeExecutable(true)
                }
            }

            triggers {
                cron(cronTrigger)
            }

        }
    }

    static def basicPipeline(job, repo, includeBranches = "master *.x-maintenance PR-*", ignoreOnPush = false,
                             buildPR = true, jenkinsfilePath = "Jenkinsfile", cronTrigger = 'H 23 * * *') {
        job.with {
            configure {
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

            orphanedItemStrategy {
                discardOldItems {
                    daysToKeep(1)
                }
            }

            triggers {
                cron(cronTrigger)
            }

        }
    }
}