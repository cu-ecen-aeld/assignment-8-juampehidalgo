# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f098732a73b5f6f3430472f5b094ffdb"

SRC_URI = "git://github.com/cu-ecen-aeld/ldd3.git;protocol=https;branch=master \
           file://0001-Makefile-modification-to-only-build-scull.patch \
           file://S98scull \
           file://scull_load \
           file://scull_unload \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "718cbdf07e082486e008537660e6b9fb4fb07a45"

S = "${WORKDIR}/git"

inherit module

# Since the repository contains code for multiple drivers, S will point to the root and we need to fetch the objects from ./scull
# So modify the taSsk-install below to reflect this.
EXTRA_OEMAKE:append:task-install = " -C ${STAGING_KERNEL_DIR} M=${S}/scull"
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

inherit update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "S98scull"

do_configure () {
    :
}

do_compile () {
    oe_runmake
}

do_install () {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/S98scull ${D}${sysconfdir}/init.d/S98scull
    install -d ${D}${base_sbindir}
	install -m 0755 ${WORKDIR}/scull_load ${D}${base_sbindir}/scull_load
	install -m 0755 ${WORKDIR}/scull_unload ${D}${base_sbindir}/scull_unload
    install -d ${D}/lib/modules/${KERNEL_VERSION}/extra
    install -m 0755 ${S}/scull/scull.ko ${D}/lib/modules/${KERNEL_VERSION}/extra

}

# For some reason, this must appear after the install step

FILES:${PN} += "${base_sbindir}"
FILES:${PN} += "${base_sbindir}/scull_load"
FILES:${PN} += "${base_sbindir}/scull_unload"
FILES:${PN} += "${sysconfdir}/init.d"
FILES:${PN} += "${sysconfdir}/init.d/S98scull"
