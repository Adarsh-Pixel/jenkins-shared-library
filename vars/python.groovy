def lintchecks() {
                    sh "echo Installing Pylint"
                    sh "pylint mymodule.py || true"
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

                stage('Generating Artifacts') {
                    steps {
                        sh "echo Generating Artifacts for $COMPONENT"
                        sh "pip install pylint"

                    }
                }
            }
        }
    }