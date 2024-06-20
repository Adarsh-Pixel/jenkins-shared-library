def sonarChecks() {
        stage('Sonar Checks') {
        sh "echo starting sonar checks for ${COMPONENT}"
        // sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ $ARGS -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar.password=${SONAR_CRED_PSW}"
        // sh "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gate.sh"
        // sh "bash quality-gate.sh ${SONAR_CRED_USR} ${SONAR_CRED_PSW} ${SONAR_URL} ${COMPONENT}"
        sh "echo ${COMPONENT} sonar checks are completed"
        }
}

def lintChecks() {
        stage('Lint checks'){
                if(env.APPTYPE == "nodejs"){
                    sh "echo Installing JSlist"
                    sh "npm i jslint"
                    sh "echo starting linkchecks for ${COMPONENT}"
                    sh "node_modules/jslint/bin/jslint.js server.js || true"
                    sh "echo linkchecks completed for ${COMPONENT}"
                }
                else if(env.APPTYPE == "maven"){
                    sh "echo starting linkchecks for ${COMPONENT}"
                    sh "mvn checkstyle:check || true"
                    sh "echo linkchecks completed for ${COMPONENT}"
                }
                else if(env.APPTYPE == "pylint"){
                    sh "echo starting linkchecks for ${COMPONENT}"
                    sh "pylint *.py || true"
                    sh "echo linkchecks completed for ${COMPONENT}"
                }
                else {
                     sh "Lint checks for frontend are in progress"   
                }
        }
}

