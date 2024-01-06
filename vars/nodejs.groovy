def call () {
        pipeline {
            agent any 
            stages {
                stage('Lint Checks') {
                    steps {
                        sh "echo Installing JSlist"
                        sh "npm i jslint"
                        sh "echo starting linkchecks ..."
                        sh "node_modules/jslint/bin/jslint.js server.js || true"
                        sh "echo linkchecks completed"
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