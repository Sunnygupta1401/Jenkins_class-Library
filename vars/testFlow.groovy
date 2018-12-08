def call(Map pipelineParams) {

    pipeline {
        agent any
        stages {
            stage('checkout git') {
                steps {
                    git branch: pipelineParams.branch, credentialsId: 'GitCredentials', url: pipelineParams.scmUrl
                }
            }

            stage('build') {
                steps {
                    //sh 'mvn clean package -DskipTests=true'
                    echo "build"
                }
            }

            stage ('test') {
                steps {
                    echo "test started"
                }
            }

            stage('deploy developmentServer'){
                steps {
                    echo "dev started"
                }
            }

            stage('deploy staging'){
                steps {
                    echo "stag started"
                }
            }

            stage('deploy production'){
                steps {
                    echo "prod started"
                }
            }
        }
        post {
            failure {
                mail to: pipelineParams.email, subject: 'Pipeline failed', body: "${env.BUILD_URL}"
            }
        }
    }
}
