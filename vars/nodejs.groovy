def lintchecks() {
                    sh "echo Installing JSlist"
                    sh "npm i jslint"
                    sh "echo starting linkchecks for ${COMPONENT}"
                    sh "node_modules/jslint/bin/jslint.js server.js || true"
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
                        sh "echo Generating Artifacts...."
                        sh "npm install"

                    }
                }
            }
        }
    }