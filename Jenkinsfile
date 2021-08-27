def app_name = "frontend"

pipeline {
    agent {
        kubernetes {
            yamlFile 'agent.yaml'
        }
    }
    stages {
        stage('Build') {
            steps {
                container('maven') {
                    sh 'mvn clean package -Dmaven.test.failure.ignore=true -DskipTests=true'
                }
            }
        }
//        stage('Sonar analyse') {
//            steps {
//                container('maven') {
//                    withSonarQubeEnv('Sonar') {
//                        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar -DskipTests=true'
//                    }
//                }
//            }
//        }
//        stage('Unit Tests') {
//           steps {
//               container('docker') {
//                   script {
//                       docker.image('mysql:8').withRun('-e "MYSQL_ROOT_PASSWORD=pass_word" -e "MYSQL_DATABASE=covid_tracker" -p 3306:3306') { c ->
//                           docker.image('mysql:8').inside("--link ${c.id}:db") {
//                               sh 'while ! mysqladmin ping -hdb --silent; do sleep 1; done'
//                           }
//                           docker.image('maven:3-openjdk-11').inside("--link ${c.id}:db") {
//                               sh 'mvn test -DskipTests=false -Dspring.jpa.hibernate.ddl-auto=create -Dspring.datasource.url=jdbc:mysql://db/covid_tracker -Dspring.datasource.username=root -Dspring.datasource.password=pass_word'
//                           }
//                       }
//                   }
//               }
//           }
//        }
        stage('Create image') {
            steps {
                script {
                    container('docker') {
                        def shaSum = env.GIT_COMMIT
                        def branch = env.GIT_BRANCH
                        def shortSum = shaSum.substring(0,7)
                        if (branch.contains("/")) {
                            branch = branch.split("/")[1]
                        }
                        def tag = "${branch}-${shortSum}"

                        def repository = ""
                        if (branch == 'main') {
                            repository = "${app_name}"
                        } else {
                            repository = "${app_name}-dev"
                        }
                        docker.withRegistry("https://${env.ECR}",'ecr:eu-central-1:JenkinsECR') {
                            def image = docker.build("${repository}:${tag}")
                            image.push()
                            image.push('latest')
                        }
                    }
                }
            }

        }
        stage('Deploy') {
            steps {
                script {
                    container('jnlp') {
                        def branch = env.GIT_BRANCH
                        if (branch.contains("main")) {
                            sh 'sed -i "s/__NAMESPACE__/app-prod/g" cicd/k8s.yaml'
                            sh 'sed -i "s/__IMAGE__/frontend/g" cicd/k8s.yaml'
                        } else {
                            sh 'sed -i "s/__NAMESPACE__/app-dev/g" cicd/k8s.yaml'
                            sh 'sed -i "s/__IMAGE__/frontend-dev/g" cicd/k8s.yaml'
                        }
                        sh 'sed -i "s/__ECR__/${ECR}/g" cicd/k8s.yaml'
                        kubernetesDeploy(configs: "cicd/k8s.yaml", kubeconfigId: "k8s")
                    }
                }
            }
        }
    }
}