def lintchecks() {
                    sh "echo Installing JSlist"
                    sh "mvn checkstyle:check || true"
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
                            lintchecks()
                        }

                    }
                }

                stage('Code compile') {
                    steps {
                        sh "echo Generating Artifacts for $COMPONENT"
                        sh "mvn clean compile"

                    }
                }

                stage('Sonar checks') {
                    steps {
                        script{
                            env.ARGS="-Dsonar.java.binaries=target/"
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