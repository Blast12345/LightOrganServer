package config

var ConfigSingleton = ConfigStorage().get() ?: Config()