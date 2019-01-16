package me.jinuo.backend.flyway

import org.flywaydb.gradle.FlywayExtension
import org.flywaydb.gradle.FlywayPlugin
import org.flywaydb.gradle.task.FlywayBaselineTask
import org.flywaydb.gradle.task.FlywayInfoTask
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.flywaydb.gradle.task.FlywayRepairTask
import org.flywaydb.gradle.task.FlywayValidateTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.tasks.userinput.UserInputHandler

class FlywayCenterPlugin implements Plugin<Project> {
    Project project

    @Override
    void apply(Project project) {
        this.project = project
        project.getPluginManager().apply(FlywayPlugin)
        log("flyway_center plugin")
        project.extensions.create("flyway_center", FlywayCenterExtension, project)
        project.afterEvaluate {
            log("search flyway_center extension")
            addTask(project.extensions.findByName("flyway_center"))
        }
    }

    def addTask(FlywayCenterExtension extension) {
        log("add flyway center task")
        FlywayExtension flywayExtension = project.extensions.findByName("flyway")
        flywayExtension.driver = extension.driver
        flywayExtension.cleanDisabled = true
        flywayExtension.locations = ['db.migration']

        extension.env.each { env ->
            createInfoTask(env, flywayExtension)
            createMigrateTask(env, flywayExtension)
            createBaselineTask(env, flywayExtension)
            createValidateTask(env, flywayExtension)
            createRepairTask(env, flywayExtension)
        }
    }

    def createInfoTask(DatabaseEnv env, FlywayExtension flywayExtension) {
        log("createInfoTask")
        def task = project.tasks.create("info" + env.name.capitalize(), FlywayInfoTask)
        task.group = "flyway center"
        task.dependsOn project.tasks.findByName("build")
        task.doFirst {
            updateFlyway('validate', env, flywayExtension)
        }
    }

    def createMigrateTask(DatabaseEnv env, FlywayExtension flywayExtension) {
        log("createMigrateTask")
        def task = project.tasks.create("migrate" + env.name.capitalize(), FlywayMigrateTask)
        task.group = "flyway center"
        task.dependsOn project.tasks.findByName("build")
        task.doFirst {
            updateFlyway('migrate', env, flywayExtension, true)
        }
    }

    def createBaselineTask(DatabaseEnv env, FlywayExtension flywayExtension) {
        log("createBaselineTask")
        def task = project.tasks.create("baseline" + env.name.capitalize(), FlywayBaselineTask)
        task.group = "flyway center"
        task.dependsOn project.tasks.findByName("build")
        task.doFirst {
            updateFlyway('baseline', env, flywayExtension, true)
        }
    }

    def createValidateTask(DatabaseEnv env, FlywayExtension flywayExtension) {
        log("createValidateTask")
        def task = project.tasks.create("validate" + env.name.capitalize(), FlywayValidateTask)
        task.group = "flyway center"
        task.dependsOn project.tasks.findByName("build")
        task.doFirst {
            updateFlyway('validate', env, flywayExtension)
        }
    }

    def createRepairTask(DatabaseEnv env, FlywayExtension flywayExtension) {
        log("createValidateTask")
        def task = project.tasks.create("repair" + env.name.capitalize(), FlywayRepairTask)
        task.group = "flyway center"
        task.dependsOn project.tasks.findByName("build")
        task.doFirst {
            updateFlyway('validate', env, flywayExtension, true)
        }
    }

    def updateFlyway(String action, DatabaseEnv env, FlywayExtension flywayExtension, boolean dangerous = false) {
        if (dangerous && env.protect){
            def userInputHandler = project.services.get(UserInputHandler)
            def response = userInputHandler.askQuestion("Please enter \'$action $env.name\' to comfirm dangerous action: ","no")
            if (response != "$action $env.name"){
                throw new IllegalAccessError('Dangerous action confirm failed')
            }
        }
        flywayExtension.url = env.url
        flywayExtension.target = env.target

        if (!env.user || env.user.isEmpty()){
            def userInputHandler = project.services.get(UserInputHandler)
            flywayExtension.user = userInputHandler.askQuestion("Please enter \'$env.name\' user: ",null)
        }else{
            flywayExtension.user = env.user
        }

        if (!env.password || env.password.isEmpty()){
            def userInputHandler = project.services.get(UserInputHandler)
            flywayExtension.password = userInputHandler.askQuestion("Please enter \'$env.name\' password: ",null)
        }else{
            flywayExtension.password = env.password
        }
    }


    def log(String message) {
        project.logger.info("[flyway center] $message")
    }
}
