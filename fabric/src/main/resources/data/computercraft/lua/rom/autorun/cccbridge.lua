local motd = settings.get("motd.path") or ""
motd = motd..":/rom/cccbridge_motd.txt"
settings.set("motd.path", motd)