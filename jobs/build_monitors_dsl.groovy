println 'Configuring build lists/monitors'

buildMonitorView('Generator plus develop') {
    statusFilter(StatusFilter.ALL)
    recurse(true)

    jobs {
        // Pipelines
        [
                'develop'
        ].each { branch ->
            name("Demo/Spring Boot App Multibranch Pipeline/$branch")
        }

        name('Jenkins jobs generator')
    }
}

buildMonitorView('Generator plus main') {
    statusFilter(StatusFilter.ALL)
    recurse(true)

    jobs {
        // Pipelines
        [
                'main'
        ].each { branch ->
            name("Demo/Spring Boot App Multibranch Pipeline/$branch")
        }

        name('Jenkins jobs generator')
    }
}