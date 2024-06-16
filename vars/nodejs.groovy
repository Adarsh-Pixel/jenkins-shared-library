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
                NEXUS_URL = "172.31.45.234"
                SONAR_CRED = credentials('SONAR_CRED')
                NEXUS_CRED = credentials('NEXUS_CRED')
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

                stage ('Check the Release') {
                    when {
                        expression { env.TAG_NAME != null }
                    }
                    steps {
                        script {
                        def UPLOAD_STATUS=sh(returnStdout: true, script: "curl http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}")

                        }
                    }
                }

                stage('Generating Artifacts') {
                    when {
                        expression { env.TAG_NAME != null }
                    }
                    steps {
                        sh "echo Generating Artifacts...."
                        sh "npm install"
                        sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
                        sh "ls -ltr"
                    }
                }
                stage('Uploading Artifacts') {
                    when {
                        expression { env.TAG_NAME != null }
                    }
                    steps {
                        sh '''
                        echo Uploading ${COMPONENT} artifact to nexus
                        curl -v -u ${NEXUS_CRED_USR}:${NEXUS_CRED_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
                        echo Uploading ${COMPONENT} artifact to nexus is completed
                        '''
                    }
                }


            }
        }
    }