package me.jinuo.backend.flyway

class DatabaseEnv {
    String name
    String url
    String user
    String password
    String target
    boolean protect

    DatabaseEnv(String name) {
        this.name = name
    }

    @Override
    String toString() {
        return "name = $name" +
                "\nurl = $url" +
                "\nuser = $user" +
                "\npassword = $password"+
                "\ntarget = $target" +
                "\nprotect = $protect"
    }
}
