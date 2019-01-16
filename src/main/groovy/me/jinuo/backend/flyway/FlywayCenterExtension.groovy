package me.jinuo.backend.flyway;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project;

class FlywayCenterExtension {
    String driver
    transient final NamedDomainObjectContainer<DatabaseEnv> env

    FlywayCenterExtension(Project project) {
        env = project.container(DatabaseEnv)
    }

    void env(Action<? super NamedDomainObjectContainer<DatabaseEnv>> action){
        action.execute(env)
    }
}
