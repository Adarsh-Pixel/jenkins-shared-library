def call() {
    node {
        git branch: 'main', url: "https://github.com/Adarsh-Pixel/${COMPONENT}.git"
        common.lintChecks()
        env.ARGS="-Dsonar.sources=."
        common.sonarChecks()
        common.testCases()
        common.artifacts()
    }
}