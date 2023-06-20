# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-juampehidalgo.git;protocol=ssh;branch=master \
           file://S99aesdchar \
           file://0001-Kernel-modules-are-placed-in-the-extra-subfolder-in-.patch;patchdir=.. \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "2faa1c943ed85af9ac3698bb93514e386002fe7d"

S = "${WORKDIR}/git/aesd-char-driver"

inherit module

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "S99aesdchar"

do_configure () {
	:
}

do_compile () {
	oe_runmake
}

do_install () {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/S99aesdchar ${D}${sysconfdir}/init.d/S99aesdchar
	install -d ${D}${base_sbindir}
	install -m 0755 ${S}/aesdchar.init ${D}${base_sbindir}/aesdchar.init
	install -d ${D}/lib/modules/${KERNEL_VERSION}/extra
	install -m 0755 ${S}/aesdchar.ko ${D}/lib/modules/${KERNEL_VERSION}/extra
}

FILES:${PN} += "${base_sbindir}"
FILES:${PN} += "${base_sbindir}/aesdchar.init"
FILES:${PN} += "${sysconfdir}/init.d"
FILES:${PN} += "${sysconfdir}/init.d/S99aesdchar"
