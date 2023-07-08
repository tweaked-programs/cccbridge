local motd = settings.get("motd.path") or ""
settings.set("motd.path", motd..":/rom/cccbridge_motd.txt")