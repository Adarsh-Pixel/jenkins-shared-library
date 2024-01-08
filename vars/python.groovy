def lintchecks() {
                    sh "echo Installing Pylint"
                    sh "pylint payment.py || true"
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

            }
        }
    }
