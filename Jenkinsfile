#!/usr/bin/env groovy

pipeline {
    agent any
    tools {
        jdk "jdk-17.0.1"
    }

    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }

        stage('Build') {
            steps {
                echo 'Building'
                sh './gradlew build'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests'
                sh './gradlew check'
            }
        }

        stage('Publish') {
            steps {
                echo 'Deploying to Maven'
                sh './gradlew publish'
            }
        }
    }

    post {
        always {
            junit 'build/test-results/**/*.xml'
        }
    }
    options {
        disableConcurrentBuilds()
    }
}
