def lintchecks() {
                    sh "echo Installing JSlist"
                    sh "mvn checkstyle:check || true"
                    sh "echo linkchecks completed for ${COMPONENT}"
}

def call () {
        pipeline {
            agent any 
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
            }
        }
    }