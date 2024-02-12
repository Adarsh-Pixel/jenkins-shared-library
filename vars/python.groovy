def lintChecks() {
        sh "echo Starting lintChecks for ${COMPONENT}"
        sh "pylint *.py || true"
        sh "echo linkchecks completed for ${COMPONENT}"
}

def call () {
        pipeline {
            agent any 
            environment {
                SONAR_URL = "172.31.23.141"
                SONAR_CRED = credentials('SONAR_CRED')
            }
            stages {
                stage('Lint Checks') {
                    steps {
                        script{
                            lintChecks()
                        }

                    }
                }
                stage('Sonar checks') {
                    steps {
                        script{
                            env.ARGS="-Dsonar.sources=."
                            common.sonarChecks()
                        }
                    }
                }
                stage('Generating artifacts') {
                    steps {
                        sh "echo Artifact generation complete"
                    }
                }
            }
        }
    }
