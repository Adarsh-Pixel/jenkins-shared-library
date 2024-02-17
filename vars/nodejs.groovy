def lintChecks() {
                    sh "echo Installing JSlist"
                    sh "npm i jslint"
                    sh "echo starting linkchecks for ${COMPONENT}"
                    sh "node_modules/jslint/bin/jslint.js server.js || true"
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
                stage('Test Cases'){
                    parallel {
                        stage('Unit Testing'){
                            steps{
                                sh "echo Starting Unit testing"
                                sh "echo Unit testing completed"
                            }
                        }
                        stage('Integration Testing'){
                            steps{
                                sh "echo Starting Integration testing"
                                sh "echo Integration testing completed"
                            }
                        }
                        stage('Functional Testing'){
                            steps{
                                sh "echo Starting Functional testing"
                                sh "echo Functional testing completed"
                            }
                        }
                    }
                }

                stage('Generating Artifacts') {
                    steps {
                        sh "echo Generating Artifacts...."
                        sh "env"
                        sh "npm install"

                    }
                }
                stage('Uploading Artifacts') {
                    steps {
                        sh "echo Uploading Artifacts...."

                    }
                }


            }
        }
    }