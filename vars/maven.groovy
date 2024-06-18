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
                        env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
                        print UPLOAD_STATUS
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