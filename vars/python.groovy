def lintChecks() {
        sh "pylint payment.py"
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
                            lintchecks()
                        }

                    }
                }
                stage('Sonar checks') {
                    steps {
                        sh "echo starting sonar checks for ${COMPONENT}"
                        sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}"
                        sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gate.sh"
                        sh "bash quality-gate.sh ${SONAR_CRED_USR} ${SONAR_CRED_PSW} ${SONAR_URL} ${COMPONENT}"
                        sh "echo ${COMPONENT} sonar checks are completed"
                    }
                }
                stage{'Generating artifacts'} {
                    steps {
                        sh "echo Artifact generation complete"
                    }
                }
            }
        }
    }
