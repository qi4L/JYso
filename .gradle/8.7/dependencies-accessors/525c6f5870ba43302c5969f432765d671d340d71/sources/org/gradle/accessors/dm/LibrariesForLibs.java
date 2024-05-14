package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final CnLibraryAccessors laccForCnLibraryAccessors = new CnLibraryAccessors(owner);
    private final ComLibraryAccessors laccForComLibraryAccessors = new ComLibraryAccessors(owner);
    private final CommonsLibraryAccessors laccForCommonsLibraryAccessors = new CommonsLibraryAccessors(owner);
    private final IoLibraryAccessors laccForIoLibraryAccessors = new IoLibraryAccessors(owner);
    private final JavaxLibraryAccessors laccForJavaxLibraryAccessors = new JavaxLibraryAccessors(owner);
    private final JbossLibraryAccessors laccForJbossLibraryAccessors = new JbossLibraryAccessors(owner);
    private final JunitLibraryAccessors laccForJunitLibraryAccessors = new JunitLibraryAccessors(owner);
    private final NetLibraryAccessors laccForNetLibraryAccessors = new NetLibraryAccessors(owner);
    private final OrgLibraryAccessors laccForOrgLibraryAccessors = new OrgLibraryAccessors(owner);
    private final RhinoLibraryAccessors laccForRhinoLibraryAccessors = new RhinoLibraryAccessors(owner);
    private final RomeLibraryAccessors laccForRomeLibraryAccessors = new RomeLibraryAccessors(owner);
    private final XercesLibraryAccessors laccForXercesLibraryAccessors = new XercesLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Group of libraries at <b>cn</b>
     */
    public CnLibraryAccessors getCn() {
        return laccForCnLibraryAccessors;
    }

    /**
     * Group of libraries at <b>com</b>
     */
    public ComLibraryAccessors getCom() {
        return laccForComLibraryAccessors;
    }

    /**
     * Group of libraries at <b>commons</b>
     */
    public CommonsLibraryAccessors getCommons() {
        return laccForCommonsLibraryAccessors;
    }

    /**
     * Group of libraries at <b>io</b>
     */
    public IoLibraryAccessors getIo() {
        return laccForIoLibraryAccessors;
    }

    /**
     * Group of libraries at <b>javax</b>
     */
    public JavaxLibraryAccessors getJavax() {
        return laccForJavaxLibraryAccessors;
    }

    /**
     * Group of libraries at <b>jboss</b>
     */
    public JbossLibraryAccessors getJboss() {
        return laccForJbossLibraryAccessors;
    }

    /**
     * Group of libraries at <b>junit</b>
     */
    public JunitLibraryAccessors getJunit() {
        return laccForJunitLibraryAccessors;
    }

    /**
     * Group of libraries at <b>net</b>
     */
    public NetLibraryAccessors getNet() {
        return laccForNetLibraryAccessors;
    }

    /**
     * Group of libraries at <b>org</b>
     */
    public OrgLibraryAccessors getOrg() {
        return laccForOrgLibraryAccessors;
    }

    /**
     * Group of libraries at <b>rhino</b>
     */
    public RhinoLibraryAccessors getRhino() {
        return laccForRhinoLibraryAccessors;
    }

    /**
     * Group of libraries at <b>rome</b>
     */
    public RomeLibraryAccessors getRome() {
        return laccForRomeLibraryAccessors;
    }

    /**
     * Group of libraries at <b>xerces</b>
     */
    public XercesLibraryAccessors getXerces() {
        return laccForXercesLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class CnLibraryAccessors extends SubDependencyFactory {
        private final CnHutoolLibraryAccessors laccForCnHutoolLibraryAccessors = new CnHutoolLibraryAccessors(owner);

        public CnLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>cn.hutool</b>
         */
        public CnHutoolLibraryAccessors getHutool() {
            return laccForCnHutoolLibraryAccessors;
        }

    }

    public static class CnHutoolLibraryAccessors extends SubDependencyFactory {
        private final CnHutoolHutoolLibraryAccessors laccForCnHutoolHutoolLibraryAccessors = new CnHutoolHutoolLibraryAccessors(owner);

        public CnHutoolLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>cn.hutool.hutool</b>
         */
        public CnHutoolHutoolLibraryAccessors getHutool() {
            return laccForCnHutoolHutoolLibraryAccessors;
        }

    }

    public static class CnHutoolHutoolLibraryAccessors extends SubDependencyFactory {

        public CnHutoolHutoolLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>all</b> with <b>cn.hutool:hutool-all</b> coordinates and
         * with version reference <b>cn.hutool.hutool.all</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAll() {
            return create("cn.hutool.hutool.all");
        }

    }

    public static class ComLibraryAccessors extends SubDependencyFactory {
        private final ComAlibabaLibraryAccessors laccForComAlibabaLibraryAccessors = new ComAlibabaLibraryAccessors(owner);
        private final ComBeustLibraryAccessors laccForComBeustLibraryAccessors = new ComBeustLibraryAccessors(owner);
        private final ComCauchoLibraryAccessors laccForComCauchoLibraryAccessors = new ComCauchoLibraryAccessors(owner);
        private final ComFasterxmlLibraryAccessors laccForComFasterxmlLibraryAccessors = new ComFasterxmlLibraryAccessors(owner);
        private final ComIbmLibraryAccessors laccForComIbmLibraryAccessors = new ComIbmLibraryAccessors(owner);
        private final ComMchangeLibraryAccessors laccForComMchangeLibraryAccessors = new ComMchangeLibraryAccessors(owner);
        private final ComNqzeroLibraryAccessors laccForComNqzeroLibraryAccessors = new ComNqzeroLibraryAccessors(owner);
        private final ComOracleLibraryAccessors laccForComOracleLibraryAccessors = new ComOracleLibraryAccessors(owner);
        private final ComTeradataLibraryAccessors laccForComTeradataLibraryAccessors = new ComTeradataLibraryAccessors(owner);
        private final ComUnboundidLibraryAccessors laccForComUnboundidLibraryAccessors = new ComUnboundidLibraryAccessors(owner);
        private final ComVaadinLibraryAccessors laccForComVaadinLibraryAccessors = new ComVaadinLibraryAccessors(owner);

        public ComLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.alibaba</b>
         */
        public ComAlibabaLibraryAccessors getAlibaba() {
            return laccForComAlibabaLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.beust</b>
         */
        public ComBeustLibraryAccessors getBeust() {
            return laccForComBeustLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.caucho</b>
         */
        public ComCauchoLibraryAccessors getCaucho() {
            return laccForComCauchoLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.fasterxml</b>
         */
        public ComFasterxmlLibraryAccessors getFasterxml() {
            return laccForComFasterxmlLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.ibm</b>
         */
        public ComIbmLibraryAccessors getIbm() {
            return laccForComIbmLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.mchange</b>
         */
        public ComMchangeLibraryAccessors getMchange() {
            return laccForComMchangeLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.nqzero</b>
         */
        public ComNqzeroLibraryAccessors getNqzero() {
            return laccForComNqzeroLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.oracle</b>
         */
        public ComOracleLibraryAccessors getOracle() {
            return laccForComOracleLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.teradata</b>
         */
        public ComTeradataLibraryAccessors getTeradata() {
            return laccForComTeradataLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.unboundid</b>
         */
        public ComUnboundidLibraryAccessors getUnboundid() {
            return laccForComUnboundidLibraryAccessors;
        }

        /**
         * Group of libraries at <b>com.vaadin</b>
         */
        public ComVaadinLibraryAccessors getVaadin() {
            return laccForComVaadinLibraryAccessors;
        }

    }

    public static class ComAlibabaLibraryAccessors extends SubDependencyFactory {
        private final ComAlibabaFastjson2LibraryAccessors laccForComAlibabaFastjson2LibraryAccessors = new ComAlibabaFastjson2LibraryAccessors(owner);

        public ComAlibabaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>fastjson</b> with <b>com.alibaba:fastjson</b> coordinates and
         * with version reference <b>com.alibaba.fastjson</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getFastjson() {
            return create("com.alibaba.fastjson");
        }

        /**
         * Group of libraries at <b>com.alibaba.fastjson2</b>
         */
        public ComAlibabaFastjson2LibraryAccessors getFastjson2() {
            return laccForComAlibabaFastjson2LibraryAccessors;
        }

    }

    public static class ComAlibabaFastjson2LibraryAccessors extends SubDependencyFactory {

        public ComAlibabaFastjson2LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>fastjson2</b> with <b>com.alibaba.fastjson2:fastjson2</b> coordinates and
         * with version reference <b>com.alibaba.fastjson2.fastjson2</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getFastjson2() {
            return create("com.alibaba.fastjson2.fastjson2");
        }

    }

    public static class ComBeustLibraryAccessors extends SubDependencyFactory {

        public ComBeustLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jcommander</b> with <b>com.beust:jcommander</b> coordinates and
         * with version reference <b>com.beust.jcommander</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJcommander() {
            return create("com.beust.jcommander");
        }

    }

    public static class ComCauchoLibraryAccessors extends SubDependencyFactory {

        public ComCauchoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>resin</b> with <b>com.caucho:resin</b> coordinates and
         * with version reference <b>com.caucho.resin</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getResin() {
            return create("com.caucho.resin");
        }

    }

    public static class ComFasterxmlLibraryAccessors extends SubDependencyFactory {
        private final ComFasterxmlJacksonLibraryAccessors laccForComFasterxmlJacksonLibraryAccessors = new ComFasterxmlJacksonLibraryAccessors(owner);

        public ComFasterxmlLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.fasterxml.jackson</b>
         */
        public ComFasterxmlJacksonLibraryAccessors getJackson() {
            return laccForComFasterxmlJacksonLibraryAccessors;
        }

    }

    public static class ComFasterxmlJacksonLibraryAccessors extends SubDependencyFactory {
        private final ComFasterxmlJacksonCoreLibraryAccessors laccForComFasterxmlJacksonCoreLibraryAccessors = new ComFasterxmlJacksonCoreLibraryAccessors(owner);

        public ComFasterxmlJacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.fasterxml.jackson.core</b>
         */
        public ComFasterxmlJacksonCoreLibraryAccessors getCore() {
            return laccForComFasterxmlJacksonCoreLibraryAccessors;
        }

    }

    public static class ComFasterxmlJacksonCoreLibraryAccessors extends SubDependencyFactory {
        private final ComFasterxmlJacksonCoreJacksonLibraryAccessors laccForComFasterxmlJacksonCoreJacksonLibraryAccessors = new ComFasterxmlJacksonCoreJacksonLibraryAccessors(owner);

        public ComFasterxmlJacksonCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.fasterxml.jackson.core.jackson</b>
         */
        public ComFasterxmlJacksonCoreJacksonLibraryAccessors getJackson() {
            return laccForComFasterxmlJacksonCoreJacksonLibraryAccessors;
        }

    }

    public static class ComFasterxmlJacksonCoreJacksonLibraryAccessors extends SubDependencyFactory {

        public ComFasterxmlJacksonCoreJacksonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>databind</b> with <b>com.fasterxml.jackson.core:jackson-databind</b> coordinates and
         * with version reference <b>com.fasterxml.jackson.core.jackson.databind</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getDatabind() {
            return create("com.fasterxml.jackson.core.jackson.databind");
        }

    }

    public static class ComIbmLibraryAccessors extends SubDependencyFactory {
        private final ComIbmWebsphereLibraryAccessors laccForComIbmWebsphereLibraryAccessors = new ComIbmWebsphereLibraryAccessors(owner);

        public ComIbmLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.ibm.websphere</b>
         */
        public ComIbmWebsphereLibraryAccessors getWebsphere() {
            return laccForComIbmWebsphereLibraryAccessors;
        }

    }

    public static class ComIbmWebsphereLibraryAccessors extends SubDependencyFactory {
        private final ComIbmWebsphereAppserverLibraryAccessors laccForComIbmWebsphereAppserverLibraryAccessors = new ComIbmWebsphereAppserverLibraryAccessors(owner);

        public ComIbmWebsphereLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.ibm.websphere.appserver</b>
         */
        public ComIbmWebsphereAppserverLibraryAccessors getAppserver() {
            return laccForComIbmWebsphereAppserverLibraryAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverLibraryAccessors extends SubDependencyFactory {
        private final ComIbmWebsphereAppserverApiLibraryAccessors laccForComIbmWebsphereAppserverApiLibraryAccessors = new ComIbmWebsphereAppserverApiLibraryAccessors(owner);

        public ComIbmWebsphereAppserverLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.ibm.websphere.appserver.api</b>
         */
        public ComIbmWebsphereAppserverApiLibraryAccessors getApi() {
            return laccForComIbmWebsphereAppserverApiLibraryAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiLibraryAccessors extends SubDependencyFactory {
        private final ComIbmWebsphereAppserverApiComLibraryAccessors laccForComIbmWebsphereAppserverApiComLibraryAccessors = new ComIbmWebsphereAppserverApiComLibraryAccessors(owner);

        public ComIbmWebsphereAppserverApiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.ibm.websphere.appserver.api.com</b>
         */
        public ComIbmWebsphereAppserverApiComLibraryAccessors getCom() {
            return laccForComIbmWebsphereAppserverApiComLibraryAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComLibraryAccessors extends SubDependencyFactory {
        private final ComIbmWebsphereAppserverApiComIbmLibraryAccessors laccForComIbmWebsphereAppserverApiComIbmLibraryAccessors = new ComIbmWebsphereAppserverApiComIbmLibraryAccessors(owner);

        public ComIbmWebsphereAppserverApiComLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.ibm.websphere.appserver.api.com.ibm</b>
         */
        public ComIbmWebsphereAppserverApiComIbmLibraryAccessors getIbm() {
            return laccForComIbmWebsphereAppserverApiComIbmLibraryAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComIbmLibraryAccessors extends SubDependencyFactory {
        private final ComIbmWebsphereAppserverApiComIbmWebsphereLibraryAccessors laccForComIbmWebsphereAppserverApiComIbmWebsphereLibraryAccessors = new ComIbmWebsphereAppserverApiComIbmWebsphereLibraryAccessors(owner);

        public ComIbmWebsphereAppserverApiComIbmLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.ibm.websphere.appserver.api.com.ibm.websphere</b>
         */
        public ComIbmWebsphereAppserverApiComIbmWebsphereLibraryAccessors getWebsphere() {
            return laccForComIbmWebsphereAppserverApiComIbmWebsphereLibraryAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComIbmWebsphereLibraryAccessors extends SubDependencyFactory {
        private final ComIbmWebsphereAppserverApiComIbmWebsphereAppserverLibraryAccessors laccForComIbmWebsphereAppserverApiComIbmWebsphereAppserverLibraryAccessors = new ComIbmWebsphereAppserverApiComIbmWebsphereAppserverLibraryAccessors(owner);

        public ComIbmWebsphereAppserverApiComIbmWebsphereLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.ibm.websphere.appserver.api.com.ibm.websphere.appserver</b>
         */
        public ComIbmWebsphereAppserverApiComIbmWebsphereAppserverLibraryAccessors getAppserver() {
            return laccForComIbmWebsphereAppserverApiComIbmWebsphereAppserverLibraryAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComIbmWebsphereAppserverLibraryAccessors extends SubDependencyFactory {
        private final ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiLibraryAccessors laccForComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiLibraryAccessors = new ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiLibraryAccessors(owner);

        public ComIbmWebsphereAppserverApiComIbmWebsphereAppserverLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.ibm.websphere.appserver.api.com.ibm.websphere.appserver.api</b>
         */
        public ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiLibraryAccessors getApi() {
            return laccForComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiLibraryAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiLibraryAccessors extends SubDependencyFactory {

        public ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>wsoc</b> with <b>com.ibm.websphere.appserver.api:com.ibm.websphere.appserver.api.wsoc</b> coordinates and
         * with version reference <b>com.ibm.websphere.appserver.api.com.ibm.websphere.appserver.api.wsoc</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWsoc() {
            return create("com.ibm.websphere.appserver.api.com.ibm.websphere.appserver.api.wsoc");
        }

    }

    public static class ComMchangeLibraryAccessors extends SubDependencyFactory {

        public ComMchangeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>c3p0</b> with <b>com.mchange:c3p0</b> coordinates and
         * with version reference <b>com.mchange.c3p0</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getC3p0() {
            return create("com.mchange.c3p0");
        }

    }

    public static class ComNqzeroLibraryAccessors extends SubDependencyFactory {
        private final ComNqzeroPermitLibraryAccessors laccForComNqzeroPermitLibraryAccessors = new ComNqzeroPermitLibraryAccessors(owner);

        public ComNqzeroLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.nqzero.permit</b>
         */
        public ComNqzeroPermitLibraryAccessors getPermit() {
            return laccForComNqzeroPermitLibraryAccessors;
        }

    }

    public static class ComNqzeroPermitLibraryAccessors extends SubDependencyFactory {

        public ComNqzeroPermitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>reflect</b> with <b>com.nqzero:permit-reflect</b> coordinates and
         * with version reference <b>com.nqzero.permit.reflect</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getReflect() {
            return create("com.nqzero.permit.reflect");
        }

    }

    public static class ComOracleLibraryAccessors extends SubDependencyFactory {
        private final ComOracleWeblogicLibraryAccessors laccForComOracleWeblogicLibraryAccessors = new ComOracleWeblogicLibraryAccessors(owner);

        public ComOracleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.oracle.weblogic</b>
         */
        public ComOracleWeblogicLibraryAccessors getWeblogic() {
            return laccForComOracleWeblogicLibraryAccessors;
        }

    }

    public static class ComOracleWeblogicLibraryAccessors extends SubDependencyFactory {
        private final ComOracleWeblogicWeblogicLibraryAccessors laccForComOracleWeblogicWeblogicLibraryAccessors = new ComOracleWeblogicWeblogicLibraryAccessors(owner);

        public ComOracleWeblogicLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.oracle.weblogic.weblogic</b>
         */
        public ComOracleWeblogicWeblogicLibraryAccessors getWeblogic() {
            return laccForComOracleWeblogicWeblogicLibraryAccessors;
        }

    }

    public static class ComOracleWeblogicWeblogicLibraryAccessors extends SubDependencyFactory {

        public ComOracleWeblogicWeblogicLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>server</b> with <b>com.oracle.weblogic:weblogic-server</b> coordinates and
         * with version reference <b>com.oracle.weblogic.weblogic.server</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getServer() {
            return create("com.oracle.weblogic.weblogic.server");
        }

    }

    public static class ComTeradataLibraryAccessors extends SubDependencyFactory {
        private final ComTeradataJdbcLibraryAccessors laccForComTeradataJdbcLibraryAccessors = new ComTeradataJdbcLibraryAccessors(owner);

        public ComTeradataLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.teradata.jdbc</b>
         */
        public ComTeradataJdbcLibraryAccessors getJdbc() {
            return laccForComTeradataJdbcLibraryAccessors;
        }

    }

    public static class ComTeradataJdbcLibraryAccessors extends SubDependencyFactory {

        public ComTeradataJdbcLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>terajdbc</b> with <b>com.teradata.jdbc:terajdbc</b> coordinates and
         * with version reference <b>com.teradata.jdbc.terajdbc</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTerajdbc() {
            return create("com.teradata.jdbc.terajdbc");
        }

    }

    public static class ComUnboundidLibraryAccessors extends SubDependencyFactory {
        private final ComUnboundidUnboundidLibraryAccessors laccForComUnboundidUnboundidLibraryAccessors = new ComUnboundidUnboundidLibraryAccessors(owner);

        public ComUnboundidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.unboundid.unboundid</b>
         */
        public ComUnboundidUnboundidLibraryAccessors getUnboundid() {
            return laccForComUnboundidUnboundidLibraryAccessors;
        }

    }

    public static class ComUnboundidUnboundidLibraryAccessors extends SubDependencyFactory {

        public ComUnboundidUnboundidLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ldapsdk</b> with <b>com.unboundid:unboundid-ldapsdk</b> coordinates and
         * with version reference <b>com.unboundid.unboundid.ldapsdk</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLdapsdk() {
            return create("com.unboundid.unboundid.ldapsdk");
        }

    }

    public static class ComVaadinLibraryAccessors extends SubDependencyFactory {
        private final ComVaadinVaadinLibraryAccessors laccForComVaadinVaadinLibraryAccessors = new ComVaadinVaadinLibraryAccessors(owner);

        public ComVaadinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>com.vaadin.vaadin</b>
         */
        public ComVaadinVaadinLibraryAccessors getVaadin() {
            return laccForComVaadinVaadinLibraryAccessors;
        }

    }

    public static class ComVaadinVaadinLibraryAccessors extends SubDependencyFactory {

        public ComVaadinVaadinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>server</b> with <b>com.vaadin:vaadin-server</b> coordinates and
         * with version reference <b>com.vaadin.vaadin.server</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getServer() {
            return create("com.vaadin.vaadin.server");
        }

    }

    public static class CommonsLibraryAccessors extends SubDependencyFactory {
        private final CommonsBeanutilsLibraryAccessors laccForCommonsBeanutilsLibraryAccessors = new CommonsBeanutilsLibraryAccessors(owner);
        private final CommonsCliLibraryAccessors laccForCommonsCliLibraryAccessors = new CommonsCliLibraryAccessors(owner);
        private final CommonsCollectionsLibraryAccessors laccForCommonsCollectionsLibraryAccessors = new CommonsCollectionsLibraryAccessors(owner);

        public CommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>commons.beanutils</b>
         */
        public CommonsBeanutilsLibraryAccessors getBeanutils() {
            return laccForCommonsBeanutilsLibraryAccessors;
        }

        /**
         * Group of libraries at <b>commons.cli</b>
         */
        public CommonsCliLibraryAccessors getCli() {
            return laccForCommonsCliLibraryAccessors;
        }

        /**
         * Group of libraries at <b>commons.collections</b>
         */
        public CommonsCollectionsLibraryAccessors getCollections() {
            return laccForCommonsCollectionsLibraryAccessors;
        }

    }

    public static class CommonsBeanutilsLibraryAccessors extends SubDependencyFactory {
        private final CommonsBeanutilsCommonsLibraryAccessors laccForCommonsBeanutilsCommonsLibraryAccessors = new CommonsBeanutilsCommonsLibraryAccessors(owner);

        public CommonsBeanutilsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>commons.beanutils.commons</b>
         */
        public CommonsBeanutilsCommonsLibraryAccessors getCommons() {
            return laccForCommonsBeanutilsCommonsLibraryAccessors;
        }

    }

    public static class CommonsBeanutilsCommonsLibraryAccessors extends SubDependencyFactory {

        public CommonsBeanutilsCommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>beanutils</b> with <b>commons-beanutils:commons-beanutils</b> coordinates and
         * with version reference <b>commons.beanutils.commons.beanutils</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBeanutils() {
            return create("commons.beanutils.commons.beanutils");
        }

    }

    public static class CommonsCliLibraryAccessors extends SubDependencyFactory {
        private final CommonsCliCommonsLibraryAccessors laccForCommonsCliCommonsLibraryAccessors = new CommonsCliCommonsLibraryAccessors(owner);

        public CommonsCliLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>commons.cli.commons</b>
         */
        public CommonsCliCommonsLibraryAccessors getCommons() {
            return laccForCommonsCliCommonsLibraryAccessors;
        }

    }

    public static class CommonsCliCommonsLibraryAccessors extends SubDependencyFactory {

        public CommonsCliCommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>cli</b> with <b>commons-cli:commons-cli</b> coordinates and
         * with version reference <b>commons.cli.commons.cli</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCli() {
            return create("commons.cli.commons.cli");
        }

    }

    public static class CommonsCollectionsLibraryAccessors extends SubDependencyFactory {
        private final CommonsCollectionsCommonsLibraryAccessors laccForCommonsCollectionsCommonsLibraryAccessors = new CommonsCollectionsCommonsLibraryAccessors(owner);

        public CommonsCollectionsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>commons.collections.commons</b>
         */
        public CommonsCollectionsCommonsLibraryAccessors getCommons() {
            return laccForCommonsCollectionsCommonsLibraryAccessors;
        }

    }

    public static class CommonsCollectionsCommonsLibraryAccessors extends SubDependencyFactory {

        public CommonsCollectionsCommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>collections</b> with <b>commons-collections:commons-collections</b> coordinates and
         * with version reference <b>commons.collections.commons.collections</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCollections() {
            return create("commons.collections.commons.collections");
        }

    }

    public static class IoLibraryAccessors extends SubDependencyFactory {
        private final IoProjectreactorLibraryAccessors laccForIoProjectreactorLibraryAccessors = new IoProjectreactorLibraryAccessors(owner);
        private final IoUndertowLibraryAccessors laccForIoUndertowLibraryAccessors = new IoUndertowLibraryAccessors(owner);

        public IoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>io.projectreactor</b>
         */
        public IoProjectreactorLibraryAccessors getProjectreactor() {
            return laccForIoProjectreactorLibraryAccessors;
        }

        /**
         * Group of libraries at <b>io.undertow</b>
         */
        public IoUndertowLibraryAccessors getUndertow() {
            return laccForIoUndertowLibraryAccessors;
        }

    }

    public static class IoProjectreactorLibraryAccessors extends SubDependencyFactory {
        private final IoProjectreactorReactorLibraryAccessors laccForIoProjectreactorReactorLibraryAccessors = new IoProjectreactorReactorLibraryAccessors(owner);

        public IoProjectreactorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>io.projectreactor.reactor</b>
         */
        public IoProjectreactorReactorLibraryAccessors getReactor() {
            return laccForIoProjectreactorReactorLibraryAccessors;
        }

    }

    public static class IoProjectreactorReactorLibraryAccessors extends SubDependencyFactory {

        public IoProjectreactorReactorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>io.projectreactor:reactor-core</b> coordinates and
         * with version reference <b>io.projectreactor.reactor.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("io.projectreactor.reactor.core");
        }

    }

    public static class IoUndertowLibraryAccessors extends SubDependencyFactory {
        private final IoUndertowUndertowLibraryAccessors laccForIoUndertowUndertowLibraryAccessors = new IoUndertowUndertowLibraryAccessors(owner);

        public IoUndertowLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>io.undertow.undertow</b>
         */
        public IoUndertowUndertowLibraryAccessors getUndertow() {
            return laccForIoUndertowUndertowLibraryAccessors;
        }

    }

    public static class IoUndertowUndertowLibraryAccessors extends SubDependencyFactory {

        public IoUndertowUndertowLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>io.undertow:undertow-core</b> coordinates and
         * with version reference <b>io.undertow.undertow.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("io.undertow.undertow.core");
        }

        /**
         * Dependency provider for <b>servlet</b> with <b>io.undertow:undertow-servlet</b> coordinates and
         * with version reference <b>io.undertow.undertow.servlet</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getServlet() {
            return create("io.undertow.undertow.servlet");
        }

    }

    public static class JavaxLibraryAccessors extends SubDependencyFactory {
        private final JavaxMediaLibraryAccessors laccForJavaxMediaLibraryAccessors = new JavaxMediaLibraryAccessors(owner);
        private final JavaxServletLibraryAccessors laccForJavaxServletLibraryAccessors = new JavaxServletLibraryAccessors(owner);
        private final JavaxWebsocketLibraryAccessors laccForJavaxWebsocketLibraryAccessors = new JavaxWebsocketLibraryAccessors(owner);

        public JavaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.media</b>
         */
        public JavaxMediaLibraryAccessors getMedia() {
            return laccForJavaxMediaLibraryAccessors;
        }

        /**
         * Group of libraries at <b>javax.servlet</b>
         */
        public JavaxServletLibraryAccessors getServlet() {
            return laccForJavaxServletLibraryAccessors;
        }

        /**
         * Group of libraries at <b>javax.websocket</b>
         */
        public JavaxWebsocketLibraryAccessors getWebsocket() {
            return laccForJavaxWebsocketLibraryAccessors;
        }

    }

    public static class JavaxMediaLibraryAccessors extends SubDependencyFactory {
        private final JavaxMediaJaiLibraryAccessors laccForJavaxMediaJaiLibraryAccessors = new JavaxMediaJaiLibraryAccessors(owner);

        public JavaxMediaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.media.jai</b>
         */
        public JavaxMediaJaiLibraryAccessors getJai() {
            return laccForJavaxMediaJaiLibraryAccessors;
        }

    }

    public static class JavaxMediaJaiLibraryAccessors extends SubDependencyFactory {
        private final JavaxMediaJaiJaiLibraryAccessors laccForJavaxMediaJaiJaiLibraryAccessors = new JavaxMediaJaiJaiLibraryAccessors(owner);

        public JavaxMediaJaiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.media.jai.jai</b>
         */
        public JavaxMediaJaiJaiLibraryAccessors getJai() {
            return laccForJavaxMediaJaiJaiLibraryAccessors;
        }

    }

    public static class JavaxMediaJaiJaiLibraryAccessors extends SubDependencyFactory {

        public JavaxMediaJaiJaiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>javax.media.jai:jai-core</b> coordinates and
         * with version reference <b>javax.media.jai.jai.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("javax.media.jai.jai.core");
        }

    }

    public static class JavaxServletLibraryAccessors extends SubDependencyFactory {
        private final JavaxServletJavaxLibraryAccessors laccForJavaxServletJavaxLibraryAccessors = new JavaxServletJavaxLibraryAccessors(owner);

        public JavaxServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.servlet.javax</b>
         */
        public JavaxServletJavaxLibraryAccessors getJavax() {
            return laccForJavaxServletJavaxLibraryAccessors;
        }

    }

    public static class JavaxServletJavaxLibraryAccessors extends SubDependencyFactory {
        private final JavaxServletJavaxServletLibraryAccessors laccForJavaxServletJavaxServletLibraryAccessors = new JavaxServletJavaxServletLibraryAccessors(owner);

        public JavaxServletJavaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.servlet.javax.servlet</b>
         */
        public JavaxServletJavaxServletLibraryAccessors getServlet() {
            return laccForJavaxServletJavaxServletLibraryAccessors;
        }

    }

    public static class JavaxServletJavaxServletLibraryAccessors extends SubDependencyFactory {

        public JavaxServletJavaxServletLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>javax.servlet:javax.servlet-api</b> coordinates and
         * with version reference <b>javax.servlet.javax.servlet.api</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("javax.servlet.javax.servlet.api");
        }

    }

    public static class JavaxWebsocketLibraryAccessors extends SubDependencyFactory {
        private final JavaxWebsocketJavaxLibraryAccessors laccForJavaxWebsocketJavaxLibraryAccessors = new JavaxWebsocketJavaxLibraryAccessors(owner);

        public JavaxWebsocketLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.websocket.javax</b>
         */
        public JavaxWebsocketJavaxLibraryAccessors getJavax() {
            return laccForJavaxWebsocketJavaxLibraryAccessors;
        }

    }

    public static class JavaxWebsocketJavaxLibraryAccessors extends SubDependencyFactory {
        private final JavaxWebsocketJavaxWebsocketLibraryAccessors laccForJavaxWebsocketJavaxWebsocketLibraryAccessors = new JavaxWebsocketJavaxWebsocketLibraryAccessors(owner);

        public JavaxWebsocketJavaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>javax.websocket.javax.websocket</b>
         */
        public JavaxWebsocketJavaxWebsocketLibraryAccessors getWebsocket() {
            return laccForJavaxWebsocketJavaxWebsocketLibraryAccessors;
        }

    }

    public static class JavaxWebsocketJavaxWebsocketLibraryAccessors extends SubDependencyFactory {

        public JavaxWebsocketJavaxWebsocketLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>api</b> with <b>javax.websocket:javax.websocket-api</b> coordinates and
         * with version reference <b>javax.websocket.javax.websocket.api</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getApi() {
            return create("javax.websocket.javax.websocket.api");
        }

    }

    public static class JbossLibraryAccessors extends SubDependencyFactory {
        private final JbossJbossLibraryAccessors laccForJbossJbossLibraryAccessors = new JbossJbossLibraryAccessors(owner);

        public JbossLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>jboss.jboss</b>
         */
        public JbossJbossLibraryAccessors getJboss() {
            return laccForJbossJbossLibraryAccessors;
        }

    }

    public static class JbossJbossLibraryAccessors extends SubDependencyFactory {

        public JbossJbossLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>serialization</b> with <b>jboss:jboss-serialization</b> coordinates and
         * with version reference <b>jboss.jboss.serialization</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSerialization() {
            return create("jboss.jboss.serialization");
        }

    }

    public static class JunitLibraryAccessors extends SubDependencyFactory {

        public JunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>junit</b> with <b>junit:junit</b> coordinates and
         * with version reference <b>junit.junit</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJunit() {
            return create("junit.junit");
        }

    }

    public static class NetLibraryAccessors extends SubDependencyFactory {
        private final NetJodahLibraryAccessors laccForNetJodahLibraryAccessors = new NetJodahLibraryAccessors(owner);
        private final NetSfLibraryAccessors laccForNetSfLibraryAccessors = new NetSfLibraryAccessors(owner);

        public NetLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>net.jodah</b>
         */
        public NetJodahLibraryAccessors getJodah() {
            return laccForNetJodahLibraryAccessors;
        }

        /**
         * Group of libraries at <b>net.sf</b>
         */
        public NetSfLibraryAccessors getSf() {
            return laccForNetSfLibraryAccessors;
        }

    }

    public static class NetJodahLibraryAccessors extends SubDependencyFactory {

        public NetJodahLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>expiringmap</b> with <b>net.jodah:expiringmap</b> coordinates and
         * with version reference <b>net.jodah.expiringmap</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getExpiringmap() {
            return create("net.jodah.expiringmap");
        }

    }

    public static class NetSfLibraryAccessors extends SubDependencyFactory {
        private final NetSfJsonLibraryAccessors laccForNetSfJsonLibraryAccessors = new NetSfJsonLibraryAccessors(owner);

        public NetSfLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>net.sf.json</b>
         */
        public NetSfJsonLibraryAccessors getJson() {
            return laccForNetSfJsonLibraryAccessors;
        }

    }

    public static class NetSfJsonLibraryAccessors extends SubDependencyFactory {
        private final NetSfJsonLibLibraryAccessors laccForNetSfJsonLibLibraryAccessors = new NetSfJsonLibLibraryAccessors(owner);

        public NetSfJsonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>net.sf.json.lib</b>
         */
        public NetSfJsonLibLibraryAccessors getLib() {
            return laccForNetSfJsonLibLibraryAccessors;
        }

    }

    public static class NetSfJsonLibLibraryAccessors extends SubDependencyFactory {
        private final NetSfJsonLibJsonLibraryAccessors laccForNetSfJsonLibJsonLibraryAccessors = new NetSfJsonLibJsonLibraryAccessors(owner);

        public NetSfJsonLibLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>net.sf.json.lib.json</b>
         */
        public NetSfJsonLibJsonLibraryAccessors getJson() {
            return laccForNetSfJsonLibJsonLibraryAccessors;
        }

    }

    public static class NetSfJsonLibJsonLibraryAccessors extends SubDependencyFactory {

        public NetSfJsonLibJsonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>lib</b> with <b>net.sf.json-lib:json-lib</b> coordinates and
         * with version reference <b>net.sf.json.lib.json.lib</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLib() {
            return create("net.sf.json.lib.json.lib");
        }

    }

    public static class OrgLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheLibraryAccessors laccForOrgApacheLibraryAccessors = new OrgApacheLibraryAccessors(owner);
        private final OrgAspectjLibraryAccessors laccForOrgAspectjLibraryAccessors = new OrgAspectjLibraryAccessors(owner);
        private final OrgBeanshellLibraryAccessors laccForOrgBeanshellLibraryAccessors = new OrgBeanshellLibraryAccessors(owner);
        private final OrgClojureLibraryAccessors laccForOrgClojureLibraryAccessors = new OrgClojureLibraryAccessors(owner);
        private final OrgCodehausLibraryAccessors laccForOrgCodehausLibraryAccessors = new OrgCodehausLibraryAccessors(owner);
        private final OrgEclipseLibraryAccessors laccForOrgEclipseLibraryAccessors = new OrgEclipseLibraryAccessors(owner);
        private final OrgFusesourceLibraryAccessors laccForOrgFusesourceLibraryAccessors = new OrgFusesourceLibraryAccessors(owner);
        private final OrgGlassfishLibraryAccessors laccForOrgGlassfishLibraryAccessors = new OrgGlassfishLibraryAccessors(owner);
        private final OrgHibernateLibraryAccessors laccForOrgHibernateLibraryAccessors = new OrgHibernateLibraryAccessors(owner);
        private final OrgJavassistLibraryAccessors laccForOrgJavassistLibraryAccessors = new OrgJavassistLibraryAccessors(owner);
        private final OrgJbossLibraryAccessors laccForOrgJbossLibraryAccessors = new OrgJbossLibraryAccessors(owner);
        private final OrgJenkinsLibraryAccessors laccForOrgJenkinsLibraryAccessors = new OrgJenkinsLibraryAccessors(owner);
        private final OrgOw2LibraryAccessors laccForOrgOw2LibraryAccessors = new OrgOw2LibraryAccessors(owner);
        private final OrgPythonLibraryAccessors laccForOrgPythonLibraryAccessors = new OrgPythonLibraryAccessors(owner);
        private final OrgReflectionsLibraryAccessors laccForOrgReflectionsLibraryAccessors = new OrgReflectionsLibraryAccessors(owner);
        private final OrgSpringframeworkLibraryAccessors laccForOrgSpringframeworkLibraryAccessors = new OrgSpringframeworkLibraryAccessors(owner);

        public OrgLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache</b>
         */
        public OrgApacheLibraryAccessors getApache() {
            return laccForOrgApacheLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.aspectj</b>
         */
        public OrgAspectjLibraryAccessors getAspectj() {
            return laccForOrgAspectjLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.beanshell</b>
         */
        public OrgBeanshellLibraryAccessors getBeanshell() {
            return laccForOrgBeanshellLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.clojure</b>
         */
        public OrgClojureLibraryAccessors getClojure() {
            return laccForOrgClojureLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.codehaus</b>
         */
        public OrgCodehausLibraryAccessors getCodehaus() {
            return laccForOrgCodehausLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.eclipse</b>
         */
        public OrgEclipseLibraryAccessors getEclipse() {
            return laccForOrgEclipseLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.fusesource</b>
         */
        public OrgFusesourceLibraryAccessors getFusesource() {
            return laccForOrgFusesourceLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.glassfish</b>
         */
        public OrgGlassfishLibraryAccessors getGlassfish() {
            return laccForOrgGlassfishLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.hibernate</b>
         */
        public OrgHibernateLibraryAccessors getHibernate() {
            return laccForOrgHibernateLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.javassist</b>
         */
        public OrgJavassistLibraryAccessors getJavassist() {
            return laccForOrgJavassistLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.jboss</b>
         */
        public OrgJbossLibraryAccessors getJboss() {
            return laccForOrgJbossLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.jenkins</b>
         */
        public OrgJenkinsLibraryAccessors getJenkins() {
            return laccForOrgJenkinsLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.ow2</b>
         */
        public OrgOw2LibraryAccessors getOw2() {
            return laccForOrgOw2LibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.python</b>
         */
        public OrgPythonLibraryAccessors getPython() {
            return laccForOrgPythonLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.reflections</b>
         */
        public OrgReflectionsLibraryAccessors getReflections() {
            return laccForOrgReflectionsLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.springframework</b>
         */
        public OrgSpringframeworkLibraryAccessors getSpringframework() {
            return laccForOrgSpringframeworkLibraryAccessors;
        }

    }

    public static class OrgApacheLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheClickLibraryAccessors laccForOrgApacheClickLibraryAccessors = new OrgApacheClickLibraryAccessors(owner);
        private final OrgApacheCommonsLibraryAccessors laccForOrgApacheCommonsLibraryAccessors = new OrgApacheCommonsLibraryAccessors(owner);
        private final OrgApacheLoggingLibraryAccessors laccForOrgApacheLoggingLibraryAccessors = new OrgApacheLoggingLibraryAccessors(owner);
        private final OrgApacheMavenLibraryAccessors laccForOrgApacheMavenLibraryAccessors = new OrgApacheMavenLibraryAccessors(owner);
        private final OrgApacheMyfacesLibraryAccessors laccForOrgApacheMyfacesLibraryAccessors = new OrgApacheMyfacesLibraryAccessors(owner);
        private final OrgApacheTomcatLibraryAccessors laccForOrgApacheTomcatLibraryAccessors = new OrgApacheTomcatLibraryAccessors(owner);
        private final OrgApacheWicketLibraryAccessors laccForOrgApacheWicketLibraryAccessors = new OrgApacheWicketLibraryAccessors(owner);

        public OrgApacheLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.click</b>
         */
        public OrgApacheClickLibraryAccessors getClick() {
            return laccForOrgApacheClickLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.apache.commons</b>
         */
        public OrgApacheCommonsLibraryAccessors getCommons() {
            return laccForOrgApacheCommonsLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.apache.logging</b>
         */
        public OrgApacheLoggingLibraryAccessors getLogging() {
            return laccForOrgApacheLoggingLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.apache.maven</b>
         */
        public OrgApacheMavenLibraryAccessors getMaven() {
            return laccForOrgApacheMavenLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.apache.myfaces</b>
         */
        public OrgApacheMyfacesLibraryAccessors getMyfaces() {
            return laccForOrgApacheMyfacesLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.apache.tomcat</b>
         */
        public OrgApacheTomcatLibraryAccessors getTomcat() {
            return laccForOrgApacheTomcatLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.apache.wicket</b>
         */
        public OrgApacheWicketLibraryAccessors getWicket() {
            return laccForOrgApacheWicketLibraryAccessors;
        }

    }

    public static class OrgApacheClickLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheClickClickLibraryAccessors laccForOrgApacheClickClickLibraryAccessors = new OrgApacheClickClickLibraryAccessors(owner);

        public OrgApacheClickLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.click.click</b>
         */
        public OrgApacheClickClickLibraryAccessors getClick() {
            return laccForOrgApacheClickClickLibraryAccessors;
        }

    }

    public static class OrgApacheClickClickLibraryAccessors extends SubDependencyFactory {

        public OrgApacheClickClickLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>nodeps</b> with <b>org.apache.click:click-nodeps</b> coordinates and
         * with version reference <b>org.apache.click.click.nodeps</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getNodeps() {
            return create("org.apache.click.click.nodeps");
        }

    }

    public static class OrgApacheCommonsLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheCommonsCommonsLibraryAccessors laccForOrgApacheCommonsCommonsLibraryAccessors = new OrgApacheCommonsCommonsLibraryAccessors(owner);

        public OrgApacheCommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.commons.commons</b>
         */
        public OrgApacheCommonsCommonsLibraryAccessors getCommons() {
            return laccForOrgApacheCommonsCommonsLibraryAccessors;
        }

    }

    public static class OrgApacheCommonsCommonsLibraryAccessors extends SubDependencyFactory {

        public OrgApacheCommonsCommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>collections4</b> with <b>org.apache.commons:commons-collections4</b> coordinates and
         * with version reference <b>org.apache.commons.commons.collections4</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCollections4() {
            return create("org.apache.commons.commons.collections4");
        }

        /**
         * Dependency provider for <b>lang3</b> with <b>org.apache.commons:commons-lang3</b> coordinates and
         * with version reference <b>org.apache.commons.commons.lang3</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLang3() {
            return create("org.apache.commons.commons.lang3");
        }

        /**
         * Dependency provider for <b>text</b> with <b>org.apache.commons:commons-text</b> coordinates and
         * with version reference <b>org.apache.commons.commons.text</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getText() {
            return create("org.apache.commons.commons.text");
        }

    }

    public static class OrgApacheLoggingLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheLoggingLog4jLibraryAccessors laccForOrgApacheLoggingLog4jLibraryAccessors = new OrgApacheLoggingLog4jLibraryAccessors(owner);

        public OrgApacheLoggingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.logging.log4j</b>
         */
        public OrgApacheLoggingLog4jLibraryAccessors getLog4j() {
            return laccForOrgApacheLoggingLog4jLibraryAccessors;
        }

    }

    public static class OrgApacheLoggingLog4jLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheLoggingLog4jLog4jLibraryAccessors laccForOrgApacheLoggingLog4jLog4jLibraryAccessors = new OrgApacheLoggingLog4jLog4jLibraryAccessors(owner);

        public OrgApacheLoggingLog4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.logging.log4j.log4j</b>
         */
        public OrgApacheLoggingLog4jLog4jLibraryAccessors getLog4j() {
            return laccForOrgApacheLoggingLog4jLog4jLibraryAccessors;
        }

    }

    public static class OrgApacheLoggingLog4jLog4jLibraryAccessors extends SubDependencyFactory {

        public OrgApacheLoggingLog4jLog4jLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>org.apache.logging.log4j:log4j-core</b> coordinates and
         * with version reference <b>org.apache.logging.log4j.log4j.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("org.apache.logging.log4j.log4j.core");
        }

    }

    public static class OrgApacheMavenLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheMavenPluginsLibraryAccessors laccForOrgApacheMavenPluginsLibraryAccessors = new OrgApacheMavenPluginsLibraryAccessors(owner);

        public OrgApacheMavenLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.maven.plugins</b>
         */
        public OrgApacheMavenPluginsLibraryAccessors getPlugins() {
            return laccForOrgApacheMavenPluginsLibraryAccessors;
        }

    }

    public static class OrgApacheMavenPluginsLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheMavenPluginsMavenLibraryAccessors laccForOrgApacheMavenPluginsMavenLibraryAccessors = new OrgApacheMavenPluginsMavenLibraryAccessors(owner);

        public OrgApacheMavenPluginsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.maven.plugins.maven</b>
         */
        public OrgApacheMavenPluginsMavenLibraryAccessors getMaven() {
            return laccForOrgApacheMavenPluginsMavenLibraryAccessors;
        }

    }

    public static class OrgApacheMavenPluginsMavenLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheMavenPluginsMavenAssemblyLibraryAccessors laccForOrgApacheMavenPluginsMavenAssemblyLibraryAccessors = new OrgApacheMavenPluginsMavenAssemblyLibraryAccessors(owner);

        public OrgApacheMavenPluginsMavenLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.maven.plugins.maven.assembly</b>
         */
        public OrgApacheMavenPluginsMavenAssemblyLibraryAccessors getAssembly() {
            return laccForOrgApacheMavenPluginsMavenAssemblyLibraryAccessors;
        }

    }

    public static class OrgApacheMavenPluginsMavenAssemblyLibraryAccessors extends SubDependencyFactory {

        public OrgApacheMavenPluginsMavenAssemblyLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>plugin</b> with <b>org.apache.maven.plugins:maven-assembly-plugin</b> coordinates and
         * with version reference <b>org.apache.maven.plugins.maven.assembly.plugin</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPlugin() {
            return create("org.apache.maven.plugins.maven.assembly.plugin");
        }

    }

    public static class OrgApacheMyfacesLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheMyfacesCoreLibraryAccessors laccForOrgApacheMyfacesCoreLibraryAccessors = new OrgApacheMyfacesCoreLibraryAccessors(owner);

        public OrgApacheMyfacesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.myfaces.core</b>
         */
        public OrgApacheMyfacesCoreLibraryAccessors getCore() {
            return laccForOrgApacheMyfacesCoreLibraryAccessors;
        }

    }

    public static class OrgApacheMyfacesCoreLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheMyfacesCoreMyfacesLibraryAccessors laccForOrgApacheMyfacesCoreMyfacesLibraryAccessors = new OrgApacheMyfacesCoreMyfacesLibraryAccessors(owner);

        public OrgApacheMyfacesCoreLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.myfaces.core.myfaces</b>
         */
        public OrgApacheMyfacesCoreMyfacesLibraryAccessors getMyfaces() {
            return laccForOrgApacheMyfacesCoreMyfacesLibraryAccessors;
        }

    }

    public static class OrgApacheMyfacesCoreMyfacesLibraryAccessors extends SubDependencyFactory {

        public OrgApacheMyfacesCoreMyfacesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>impl</b> with <b>org.apache.myfaces.core:myfaces-impl</b> coordinates and
         * with version reference <b>org.apache.myfaces.core.myfaces.impl</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getImpl() {
            return create("org.apache.myfaces.core.myfaces.impl");
        }

    }

    public static class OrgApacheTomcatLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheTomcatEmbedLibraryAccessors laccForOrgApacheTomcatEmbedLibraryAccessors = new OrgApacheTomcatEmbedLibraryAccessors(owner);
        private final OrgApacheTomcatTomcatLibraryAccessors laccForOrgApacheTomcatTomcatLibraryAccessors = new OrgApacheTomcatTomcatLibraryAccessors(owner);

        public OrgApacheTomcatLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.tomcat.embed</b>
         */
        public OrgApacheTomcatEmbedLibraryAccessors getEmbed() {
            return laccForOrgApacheTomcatEmbedLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.apache.tomcat.tomcat</b>
         */
        public OrgApacheTomcatTomcatLibraryAccessors getTomcat() {
            return laccForOrgApacheTomcatTomcatLibraryAccessors;
        }

    }

    public static class OrgApacheTomcatEmbedLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheTomcatEmbedTomcatLibraryAccessors laccForOrgApacheTomcatEmbedTomcatLibraryAccessors = new OrgApacheTomcatEmbedTomcatLibraryAccessors(owner);

        public OrgApacheTomcatEmbedLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.tomcat.embed.tomcat</b>
         */
        public OrgApacheTomcatEmbedTomcatLibraryAccessors getTomcat() {
            return laccForOrgApacheTomcatEmbedTomcatLibraryAccessors;
        }

    }

    public static class OrgApacheTomcatEmbedTomcatLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheTomcatEmbedTomcatEmbedLibraryAccessors laccForOrgApacheTomcatEmbedTomcatEmbedLibraryAccessors = new OrgApacheTomcatEmbedTomcatEmbedLibraryAccessors(owner);

        public OrgApacheTomcatEmbedTomcatLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.tomcat.embed.tomcat.embed</b>
         */
        public OrgApacheTomcatEmbedTomcatEmbedLibraryAccessors getEmbed() {
            return laccForOrgApacheTomcatEmbedTomcatEmbedLibraryAccessors;
        }

    }

    public static class OrgApacheTomcatEmbedTomcatEmbedLibraryAccessors extends SubDependencyFactory {

        public OrgApacheTomcatEmbedTomcatEmbedLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>org.apache.tomcat.embed:tomcat-embed-core</b> coordinates and
         * with version reference <b>org.apache.tomcat.embed.tomcat.embed.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("org.apache.tomcat.embed.tomcat.embed.core");
        }

    }

    public static class OrgApacheTomcatTomcatLibraryAccessors extends SubDependencyFactory {

        public OrgApacheTomcatTomcatLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>websocket</b> with <b>org.apache.tomcat:tomcat-websocket</b> coordinates and
         * with version reference <b>org.apache.tomcat.tomcat.websocket</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWebsocket() {
            return create("org.apache.tomcat.tomcat.websocket");
        }

    }

    public static class OrgApacheWicketLibraryAccessors extends SubDependencyFactory {
        private final OrgApacheWicketWicketLibraryAccessors laccForOrgApacheWicketWicketLibraryAccessors = new OrgApacheWicketWicketLibraryAccessors(owner);

        public OrgApacheWicketLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.apache.wicket.wicket</b>
         */
        public OrgApacheWicketWicketLibraryAccessors getWicket() {
            return laccForOrgApacheWicketWicketLibraryAccessors;
        }

    }

    public static class OrgApacheWicketWicketLibraryAccessors extends SubDependencyFactory {

        public OrgApacheWicketWicketLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>util</b> with <b>org.apache.wicket:wicket-util</b> coordinates and
         * with version reference <b>org.apache.wicket.wicket.util</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getUtil() {
            return create("org.apache.wicket.wicket.util");
        }

    }

    public static class OrgAspectjLibraryAccessors extends SubDependencyFactory {

        public OrgAspectjLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>aspectjweaver</b> with <b>org.aspectj:aspectjweaver</b> coordinates and
         * with version reference <b>org.aspectj.aspectjweaver</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAspectjweaver() {
            return create("org.aspectj.aspectjweaver");
        }

    }

    public static class OrgBeanshellLibraryAccessors extends SubDependencyFactory {

        public OrgBeanshellLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>bsh</b> with <b>org.beanshell:bsh</b> coordinates and
         * with version reference <b>org.beanshell.bsh</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBsh() {
            return create("org.beanshell.bsh");
        }

    }

    public static class OrgClojureLibraryAccessors extends SubDependencyFactory {

        public OrgClojureLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>clojure</b> with <b>org.clojure:clojure</b> coordinates and
         * with version reference <b>org.clojure.clojure</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getClojure() {
            return create("org.clojure.clojure");
        }

    }

    public static class OrgCodehausLibraryAccessors extends SubDependencyFactory {
        private final OrgCodehausGroovyLibraryAccessors laccForOrgCodehausGroovyLibraryAccessors = new OrgCodehausGroovyLibraryAccessors(owner);

        public OrgCodehausLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.codehaus.groovy</b>
         */
        public OrgCodehausGroovyLibraryAccessors getGroovy() {
            return laccForOrgCodehausGroovyLibraryAccessors;
        }

    }

    public static class OrgCodehausGroovyLibraryAccessors extends SubDependencyFactory {

        public OrgCodehausGroovyLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>groovy</b> with <b>org.codehaus.groovy:groovy</b> coordinates and
         * with version reference <b>org.codehaus.groovy.groovy</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGroovy() {
            return create("org.codehaus.groovy.groovy");
        }

    }

    public static class OrgEclipseLibraryAccessors extends SubDependencyFactory {
        private final OrgEclipseJettyLibraryAccessors laccForOrgEclipseJettyLibraryAccessors = new OrgEclipseJettyLibraryAccessors(owner);

        public OrgEclipseLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.eclipse.jetty</b>
         */
        public OrgEclipseJettyLibraryAccessors getJetty() {
            return laccForOrgEclipseJettyLibraryAccessors;
        }

    }

    public static class OrgEclipseJettyLibraryAccessors extends SubDependencyFactory {
        private final OrgEclipseJettyJettyLibraryAccessors laccForOrgEclipseJettyJettyLibraryAccessors = new OrgEclipseJettyJettyLibraryAccessors(owner);

        public OrgEclipseJettyLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.eclipse.jetty.jetty</b>
         */
        public OrgEclipseJettyJettyLibraryAccessors getJetty() {
            return laccForOrgEclipseJettyJettyLibraryAccessors;
        }

    }

    public static class OrgEclipseJettyJettyLibraryAccessors extends SubDependencyFactory {

        public OrgEclipseJettyJettyLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ant</b> with <b>org.eclipse.jetty:jetty-ant</b> coordinates and
         * with version reference <b>org.eclipse.jetty.jetty.ant</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAnt() {
            return create("org.eclipse.jetty.jetty.ant");
        }

    }

    public static class OrgFusesourceLibraryAccessors extends SubDependencyFactory {
        private final OrgFusesourceJansiLibraryAccessors laccForOrgFusesourceJansiLibraryAccessors = new OrgFusesourceJansiLibraryAccessors(owner);

        public OrgFusesourceLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.fusesource.jansi</b>
         */
        public OrgFusesourceJansiLibraryAccessors getJansi() {
            return laccForOrgFusesourceJansiLibraryAccessors;
        }

    }

    public static class OrgFusesourceJansiLibraryAccessors extends SubDependencyFactory {

        public OrgFusesourceJansiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jansi</b> with <b>org.fusesource.jansi:jansi</b> coordinates and
         * with version reference <b>org.fusesource.jansi.jansi</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJansi() {
            return create("org.fusesource.jansi.jansi");
        }

    }

    public static class OrgGlassfishLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishTyrusLibraryAccessors laccForOrgGlassfishTyrusLibraryAccessors = new OrgGlassfishTyrusLibraryAccessors(owner);

        public OrgGlassfishLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.glassfish.tyrus</b>
         */
        public OrgGlassfishTyrusLibraryAccessors getTyrus() {
            return laccForOrgGlassfishTyrusLibraryAccessors;
        }

    }

    public static class OrgGlassfishTyrusLibraryAccessors extends SubDependencyFactory {
        private final OrgGlassfishTyrusTyrusLibraryAccessors laccForOrgGlassfishTyrusTyrusLibraryAccessors = new OrgGlassfishTyrusTyrusLibraryAccessors(owner);

        public OrgGlassfishTyrusLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.glassfish.tyrus.tyrus</b>
         */
        public OrgGlassfishTyrusTyrusLibraryAccessors getTyrus() {
            return laccForOrgGlassfishTyrusTyrusLibraryAccessors;
        }

    }

    public static class OrgGlassfishTyrusTyrusLibraryAccessors extends SubDependencyFactory {

        public OrgGlassfishTyrusTyrusLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>server</b> with <b>org.glassfish.tyrus:tyrus-server</b> coordinates and
         * with version reference <b>org.glassfish.tyrus.tyrus.server</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getServer() {
            return create("org.glassfish.tyrus.tyrus.server");
        }

    }

    public static class OrgHibernateLibraryAccessors extends SubDependencyFactory {
        private final OrgHibernateHibernateLibraryAccessors laccForOrgHibernateHibernateLibraryAccessors = new OrgHibernateHibernateLibraryAccessors(owner);

        public OrgHibernateLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.hibernate.hibernate</b>
         */
        public OrgHibernateHibernateLibraryAccessors getHibernate() {
            return laccForOrgHibernateHibernateLibraryAccessors;
        }

    }

    public static class OrgHibernateHibernateLibraryAccessors extends SubDependencyFactory {

        public OrgHibernateHibernateLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>org.hibernate:hibernate-core</b> coordinates and
         * with version reference <b>org.hibernate.hibernate.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("org.hibernate.hibernate.core");
        }

    }

    public static class OrgJavassistLibraryAccessors extends SubDependencyFactory {

        public OrgJavassistLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>javassist</b> with <b>org.javassist:javassist</b> coordinates and
         * with version reference <b>org.javassist.javassist</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJavassist() {
            return create("org.javassist.javassist");
        }

    }

    public static class OrgJbossLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossInterceptorLibraryAccessors laccForOrgJbossInterceptorLibraryAccessors = new OrgJbossInterceptorLibraryAccessors(owner);
        private final OrgJbossRemotingLibraryAccessors laccForOrgJbossRemotingLibraryAccessors = new OrgJbossRemotingLibraryAccessors(owner);
        private final OrgJbossRemotingjmxLibraryAccessors laccForOrgJbossRemotingjmxLibraryAccessors = new OrgJbossRemotingjmxLibraryAccessors(owner);
        private final OrgJbossSpecLibraryAccessors laccForOrgJbossSpecLibraryAccessors = new OrgJbossSpecLibraryAccessors(owner);
        private final OrgJbossWeldLibraryAccessors laccForOrgJbossWeldLibraryAccessors = new OrgJbossWeldLibraryAccessors(owner);

        public OrgJbossLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.interceptor</b>
         */
        public OrgJbossInterceptorLibraryAccessors getInterceptor() {
            return laccForOrgJbossInterceptorLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.jboss.remoting</b>
         */
        public OrgJbossRemotingLibraryAccessors getRemoting() {
            return laccForOrgJbossRemotingLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.jboss.remotingjmx</b>
         */
        public OrgJbossRemotingjmxLibraryAccessors getRemotingjmx() {
            return laccForOrgJbossRemotingjmxLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.jboss.spec</b>
         */
        public OrgJbossSpecLibraryAccessors getSpec() {
            return laccForOrgJbossSpecLibraryAccessors;
        }

        /**
         * Group of libraries at <b>org.jboss.weld</b>
         */
        public OrgJbossWeldLibraryAccessors getWeld() {
            return laccForOrgJbossWeldLibraryAccessors;
        }

    }

    public static class OrgJbossInterceptorLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossInterceptorJbossLibraryAccessors laccForOrgJbossInterceptorJbossLibraryAccessors = new OrgJbossInterceptorJbossLibraryAccessors(owner);

        public OrgJbossInterceptorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.interceptor.jboss</b>
         */
        public OrgJbossInterceptorJbossLibraryAccessors getJboss() {
            return laccForOrgJbossInterceptorJbossLibraryAccessors;
        }

    }

    public static class OrgJbossInterceptorJbossLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossInterceptorJbossInterceptorLibraryAccessors laccForOrgJbossInterceptorJbossInterceptorLibraryAccessors = new OrgJbossInterceptorJbossInterceptorLibraryAccessors(owner);

        public OrgJbossInterceptorJbossLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.interceptor.jboss.interceptor</b>
         */
        public OrgJbossInterceptorJbossInterceptorLibraryAccessors getInterceptor() {
            return laccForOrgJbossInterceptorJbossInterceptorLibraryAccessors;
        }

    }

    public static class OrgJbossInterceptorJbossInterceptorLibraryAccessors extends SubDependencyFactory {

        public OrgJbossInterceptorJbossInterceptorLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>org.jboss.interceptor:jboss-interceptor-core</b> coordinates and
         * with version reference <b>org.jboss.interceptor.jboss.interceptor.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("org.jboss.interceptor.jboss.interceptor.core");
        }

        /**
         * Dependency provider for <b>spi</b> with <b>org.jboss.interceptor:jboss-interceptor-spi</b> coordinates and
         * with version reference <b>org.jboss.interceptor.jboss.interceptor.spi</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSpi() {
            return create("org.jboss.interceptor.jboss.interceptor.spi");
        }

    }

    public static class OrgJbossRemotingLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossRemotingJbossLibraryAccessors laccForOrgJbossRemotingJbossLibraryAccessors = new OrgJbossRemotingJbossLibraryAccessors(owner);

        public OrgJbossRemotingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.remoting.jboss</b>
         */
        public OrgJbossRemotingJbossLibraryAccessors getJboss() {
            return laccForOrgJbossRemotingJbossLibraryAccessors;
        }

    }

    public static class OrgJbossRemotingJbossLibraryAccessors extends SubDependencyFactory {

        public OrgJbossRemotingJbossLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>remoting</b> with <b>org.jboss.remoting:jboss-remoting</b> coordinates and
         * with version reference <b>org.jboss.remoting.jboss.remoting</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRemoting() {
            return create("org.jboss.remoting.jboss.remoting");
        }

    }

    public static class OrgJbossRemotingjmxLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossRemotingjmxRemotingLibraryAccessors laccForOrgJbossRemotingjmxRemotingLibraryAccessors = new OrgJbossRemotingjmxRemotingLibraryAccessors(owner);

        public OrgJbossRemotingjmxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.remotingjmx.remoting</b>
         */
        public OrgJbossRemotingjmxRemotingLibraryAccessors getRemoting() {
            return laccForOrgJbossRemotingjmxRemotingLibraryAccessors;
        }

    }

    public static class OrgJbossRemotingjmxRemotingLibraryAccessors extends SubDependencyFactory {

        public OrgJbossRemotingjmxRemotingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>jmx</b> with <b>org.jboss.remotingjmx:remoting-jmx</b> coordinates and
         * with version reference <b>org.jboss.remotingjmx.remoting.jmx</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJmx() {
            return create("org.jboss.remotingjmx.remoting.jmx");
        }

    }

    public static class OrgJbossSpecLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossSpecJavaxLibraryAccessors laccForOrgJbossSpecJavaxLibraryAccessors = new OrgJbossSpecJavaxLibraryAccessors(owner);

        public OrgJbossSpecLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.spec.javax</b>
         */
        public OrgJbossSpecJavaxLibraryAccessors getJavax() {
            return laccForOrgJbossSpecJavaxLibraryAccessors;
        }

    }

    public static class OrgJbossSpecJavaxLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossSpecJavaxSecurityLibraryAccessors laccForOrgJbossSpecJavaxSecurityLibraryAccessors = new OrgJbossSpecJavaxSecurityLibraryAccessors(owner);

        public OrgJbossSpecJavaxLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.spec.javax.security</b>
         */
        public OrgJbossSpecJavaxSecurityLibraryAccessors getSecurity() {
            return laccForOrgJbossSpecJavaxSecurityLibraryAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossSpecJavaxSecurityJaccLibraryAccessors laccForOrgJbossSpecJavaxSecurityJaccLibraryAccessors = new OrgJbossSpecJavaxSecurityJaccLibraryAccessors(owner);

        public OrgJbossSpecJavaxSecurityLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.spec.javax.security.jacc</b>
         */
        public OrgJbossSpecJavaxSecurityJaccLibraryAccessors getJacc() {
            return laccForOrgJbossSpecJavaxSecurityJaccLibraryAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossSpecJavaxSecurityJaccJbossLibraryAccessors laccForOrgJbossSpecJavaxSecurityJaccJbossLibraryAccessors = new OrgJbossSpecJavaxSecurityJaccJbossLibraryAccessors(owner);

        public OrgJbossSpecJavaxSecurityJaccLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.spec.javax.security.jacc.jboss</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossLibraryAccessors getJboss() {
            return laccForOrgJbossSpecJavaxSecurityJaccJbossLibraryAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossSpecJavaxSecurityJaccJbossJaccLibraryAccessors laccForOrgJbossSpecJavaxSecurityJaccJbossJaccLibraryAccessors = new OrgJbossSpecJavaxSecurityJaccJbossJaccLibraryAccessors(owner);

        public OrgJbossSpecJavaxSecurityJaccJbossLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.spec.javax.security.jacc.jboss.jacc</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossJaccLibraryAccessors getJacc() {
            return laccForOrgJbossSpecJavaxSecurityJaccJbossJaccLibraryAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossJaccLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossSpecJavaxSecurityJaccJbossJaccApiLibraryAccessors laccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiLibraryAccessors = new OrgJbossSpecJavaxSecurityJaccJbossJaccApiLibraryAccessors(owner);

        public OrgJbossSpecJavaxSecurityJaccJbossJaccLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.spec.javax.security.jacc.jboss.jacc.api</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiLibraryAccessors getApi() {
            return laccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiLibraryAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossJaccApiLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1LibraryAccessors laccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiV1LibraryAccessors = new OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1LibraryAccessors(owner);

        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.spec.javax.security.jacc.jboss.jacc.api.v1</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1LibraryAccessors getV1() {
            return laccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiV1LibraryAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1LibraryAccessors extends SubDependencyFactory {
        private final OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4LibraryAccessors laccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4LibraryAccessors = new OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4LibraryAccessors(owner);

        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.spec.javax.security.jacc.jboss.jacc.api.v1.v4</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4LibraryAccessors getV4() {
            return laccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4LibraryAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4LibraryAccessors extends SubDependencyFactory {

        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>spec</b> with <b>org.jboss.spec.javax.security.jacc:jboss-jacc-api_1.4_spec</b> coordinates and
         * with version reference <b>org.jboss.spec.javax.security.jacc.jboss.jacc.api.v1.v4.spec</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSpec() {
            return create("org.jboss.spec.javax.security.jacc.jboss.jacc.api.v1.v4.spec");
        }

    }

    public static class OrgJbossWeldLibraryAccessors extends SubDependencyFactory {
        private final OrgJbossWeldWeldLibraryAccessors laccForOrgJbossWeldWeldLibraryAccessors = new OrgJbossWeldWeldLibraryAccessors(owner);

        public OrgJbossWeldLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jboss.weld.weld</b>
         */
        public OrgJbossWeldWeldLibraryAccessors getWeld() {
            return laccForOrgJbossWeldWeldLibraryAccessors;
        }

    }

    public static class OrgJbossWeldWeldLibraryAccessors extends SubDependencyFactory {

        public OrgJbossWeldWeldLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>core</b> with <b>org.jboss.weld:weld-core</b> coordinates and
         * with version reference <b>org.jboss.weld.weld.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("org.jboss.weld.weld.core");
        }

    }

    public static class OrgJenkinsLibraryAccessors extends SubDependencyFactory {
        private final OrgJenkinsCiLibraryAccessors laccForOrgJenkinsCiLibraryAccessors = new OrgJenkinsCiLibraryAccessors(owner);

        public OrgJenkinsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jenkins.ci</b>
         */
        public OrgJenkinsCiLibraryAccessors getCi() {
            return laccForOrgJenkinsCiLibraryAccessors;
        }

    }

    public static class OrgJenkinsCiLibraryAccessors extends SubDependencyFactory {
        private final OrgJenkinsCiMainLibraryAccessors laccForOrgJenkinsCiMainLibraryAccessors = new OrgJenkinsCiMainLibraryAccessors(owner);

        public OrgJenkinsCiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.jenkins.ci.main</b>
         */
        public OrgJenkinsCiMainLibraryAccessors getMain() {
            return laccForOrgJenkinsCiMainLibraryAccessors;
        }

    }

    public static class OrgJenkinsCiMainLibraryAccessors extends SubDependencyFactory {

        public OrgJenkinsCiMainLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>remoting</b> with <b>org.jenkins-ci.main:remoting</b> coordinates and
         * with version reference <b>org.jenkins.ci.main.remoting</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRemoting() {
            return create("org.jenkins.ci.main.remoting");
        }

    }

    public static class OrgOw2LibraryAccessors extends SubDependencyFactory {
        private final OrgOw2AsmLibraryAccessors laccForOrgOw2AsmLibraryAccessors = new OrgOw2AsmLibraryAccessors(owner);

        public OrgOw2LibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.ow2.asm</b>
         */
        public OrgOw2AsmLibraryAccessors getAsm() {
            return laccForOrgOw2AsmLibraryAccessors;
        }

    }

    public static class OrgOw2AsmLibraryAccessors extends SubDependencyFactory {

        public OrgOw2AsmLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>asm</b> with <b>org.ow2.asm:asm</b> coordinates and
         * with version reference <b>org.ow2.asm.asm</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAsm() {
            return create("org.ow2.asm.asm");
        }

    }

    public static class OrgPythonLibraryAccessors extends SubDependencyFactory {
        private final OrgPythonJythonLibraryAccessors laccForOrgPythonJythonLibraryAccessors = new OrgPythonJythonLibraryAccessors(owner);

        public OrgPythonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.python.jython</b>
         */
        public OrgPythonJythonLibraryAccessors getJython() {
            return laccForOrgPythonJythonLibraryAccessors;
        }

    }

    public static class OrgPythonJythonLibraryAccessors extends SubDependencyFactory {

        public OrgPythonJythonLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>standalone</b> with <b>org.python:jython-standalone</b> coordinates and
         * with version reference <b>org.python.jython.standalone</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getStandalone() {
            return create("org.python.jython.standalone");
        }

    }

    public static class OrgReflectionsLibraryAccessors extends SubDependencyFactory {

        public OrgReflectionsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>reflections</b> with <b>org.reflections:reflections</b> coordinates and
         * with version reference <b>org.reflections.reflections</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getReflections() {
            return create("org.reflections.reflections");
        }

    }

    public static class OrgSpringframeworkLibraryAccessors extends SubDependencyFactory {
        private final OrgSpringframeworkSpringLibraryAccessors laccForOrgSpringframeworkSpringLibraryAccessors = new OrgSpringframeworkSpringLibraryAccessors(owner);

        public OrgSpringframeworkLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>org.springframework.spring</b>
         */
        public OrgSpringframeworkSpringLibraryAccessors getSpring() {
            return laccForOrgSpringframeworkSpringLibraryAccessors;
        }

    }

    public static class OrgSpringframeworkSpringLibraryAccessors extends SubDependencyFactory {
        private final OrgSpringframeworkSpringContextLibraryAccessors laccForOrgSpringframeworkSpringContextLibraryAccessors = new OrgSpringframeworkSpringContextLibraryAccessors(owner);

        public OrgSpringframeworkSpringLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>aop</b> with <b>org.springframework:spring-aop</b> coordinates and
         * with version reference <b>org.springframework.spring.aop</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAop() {
            return create("org.springframework.spring.aop");
        }

        /**
         * Dependency provider for <b>beans</b> with <b>org.springframework:spring-beans</b> coordinates and
         * with version reference <b>org.springframework.spring.beans</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBeans() {
            return create("org.springframework.spring.beans");
        }

        /**
         * Dependency provider for <b>core</b> with <b>org.springframework:spring-core</b> coordinates and
         * with version reference <b>org.springframework.spring.core</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("org.springframework.spring.core");
        }

        /**
         * Dependency provider for <b>jdbc</b> with <b>org.springframework:spring-jdbc</b> coordinates and
         * with version reference <b>org.springframework.spring.jdbc</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJdbc() {
            return create("org.springframework.spring.jdbc");
        }

        /**
         * Dependency provider for <b>oxm</b> with <b>org.springframework:spring-oxm</b> coordinates and
         * with version reference <b>org.springframework.spring.oxm</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getOxm() {
            return create("org.springframework.spring.oxm");
        }

        /**
         * Dependency provider for <b>test</b> with <b>org.springframework:spring-test</b> coordinates and
         * with version reference <b>org.springframework.spring.test</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTest() {
            return create("org.springframework.spring.test");
        }

        /**
         * Dependency provider for <b>tx</b> with <b>org.springframework:spring-tx</b> coordinates and
         * with version reference <b>org.springframework.spring.tx</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTx() {
            return create("org.springframework.spring.tx");
        }

        /**
         * Dependency provider for <b>web</b> with <b>org.springframework:spring-web</b> coordinates and
         * with version reference <b>org.springframework.spring.web</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWeb() {
            return create("org.springframework.spring.web");
        }

        /**
         * Dependency provider for <b>webmvc</b> with <b>org.springframework:spring-webmvc</b> coordinates and
         * with version reference <b>org.springframework.spring.webmvc</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getWebmvc() {
            return create("org.springframework.spring.webmvc");
        }

        /**
         * Group of libraries at <b>org.springframework.spring.context</b>
         */
        public OrgSpringframeworkSpringContextLibraryAccessors getContext() {
            return laccForOrgSpringframeworkSpringContextLibraryAccessors;
        }

    }

    public static class OrgSpringframeworkSpringContextLibraryAccessors extends SubDependencyFactory {

        public OrgSpringframeworkSpringContextLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>support</b> with <b>org.springframework:spring-context-support</b> coordinates and
         * with version reference <b>org.springframework.spring.context.support</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSupport() {
            return create("org.springframework.spring.context.support");
        }

    }

    public static class RhinoLibraryAccessors extends SubDependencyFactory {

        public RhinoLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>js</b> with <b>rhino:js</b> coordinates and
         * with version reference <b>rhino.js</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJs() {
            return create("rhino.js");
        }

    }

    public static class RomeLibraryAccessors extends SubDependencyFactory {

        public RomeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>rome</b> with <b>rome:rome</b> coordinates and
         * with version reference <b>rome.rome</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRome() {
            return create("rome.rome");
        }

    }

    public static class XercesLibraryAccessors extends SubDependencyFactory {

        public XercesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>xercesimpl</b> with <b>xerces:xercesImpl</b> coordinates and
         * with version reference <b>xerces.xercesimpl</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getXercesimpl() {
            return create("xerces.xercesimpl");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final CnVersionAccessors vaccForCnVersionAccessors = new CnVersionAccessors(providers, config);
        private final ComVersionAccessors vaccForComVersionAccessors = new ComVersionAccessors(providers, config);
        private final CommonsVersionAccessors vaccForCommonsVersionAccessors = new CommonsVersionAccessors(providers, config);
        private final IoVersionAccessors vaccForIoVersionAccessors = new IoVersionAccessors(providers, config);
        private final JavaxVersionAccessors vaccForJavaxVersionAccessors = new JavaxVersionAccessors(providers, config);
        private final JbossVersionAccessors vaccForJbossVersionAccessors = new JbossVersionAccessors(providers, config);
        private final JunitVersionAccessors vaccForJunitVersionAccessors = new JunitVersionAccessors(providers, config);
        private final NetVersionAccessors vaccForNetVersionAccessors = new NetVersionAccessors(providers, config);
        private final OrgVersionAccessors vaccForOrgVersionAccessors = new OrgVersionAccessors(providers, config);
        private final RhinoVersionAccessors vaccForRhinoVersionAccessors = new RhinoVersionAccessors(providers, config);
        private final RomeVersionAccessors vaccForRomeVersionAccessors = new RomeVersionAccessors(providers, config);
        private final XercesVersionAccessors vaccForXercesVersionAccessors = new XercesVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.cn</b>
         */
        public CnVersionAccessors getCn() {
            return vaccForCnVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com</b>
         */
        public ComVersionAccessors getCom() {
            return vaccForComVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.commons</b>
         */
        public CommonsVersionAccessors getCommons() {
            return vaccForCommonsVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.io</b>
         */
        public IoVersionAccessors getIo() {
            return vaccForIoVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.javax</b>
         */
        public JavaxVersionAccessors getJavax() {
            return vaccForJavaxVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.jboss</b>
         */
        public JbossVersionAccessors getJboss() {
            return vaccForJbossVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.junit</b>
         */
        public JunitVersionAccessors getJunit() {
            return vaccForJunitVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.net</b>
         */
        public NetVersionAccessors getNet() {
            return vaccForNetVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org</b>
         */
        public OrgVersionAccessors getOrg() {
            return vaccForOrgVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.rhino</b>
         */
        public RhinoVersionAccessors getRhino() {
            return vaccForRhinoVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.rome</b>
         */
        public RomeVersionAccessors getRome() {
            return vaccForRomeVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.xerces</b>
         */
        public XercesVersionAccessors getXerces() {
            return vaccForXercesVersionAccessors;
        }

    }

    public static class CnVersionAccessors extends VersionFactory  {

        private final CnHutoolVersionAccessors vaccForCnHutoolVersionAccessors = new CnHutoolVersionAccessors(providers, config);
        public CnVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.cn.hutool</b>
         */
        public CnHutoolVersionAccessors getHutool() {
            return vaccForCnHutoolVersionAccessors;
        }

    }

    public static class CnHutoolVersionAccessors extends VersionFactory  {

        private final CnHutoolHutoolVersionAccessors vaccForCnHutoolHutoolVersionAccessors = new CnHutoolHutoolVersionAccessors(providers, config);
        public CnHutoolVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.cn.hutool.hutool</b>
         */
        public CnHutoolHutoolVersionAccessors getHutool() {
            return vaccForCnHutoolHutoolVersionAccessors;
        }

    }

    public static class CnHutoolHutoolVersionAccessors extends VersionFactory  {

        public CnHutoolHutoolVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>cn.hutool.hutool.all</b> with value <b>5.7.7</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAll() { return getVersion("cn.hutool.hutool.all"); }

    }

    public static class ComVersionAccessors extends VersionFactory  {

        private final ComAlibabaVersionAccessors vaccForComAlibabaVersionAccessors = new ComAlibabaVersionAccessors(providers, config);
        private final ComBeustVersionAccessors vaccForComBeustVersionAccessors = new ComBeustVersionAccessors(providers, config);
        private final ComCauchoVersionAccessors vaccForComCauchoVersionAccessors = new ComCauchoVersionAccessors(providers, config);
        private final ComFasterxmlVersionAccessors vaccForComFasterxmlVersionAccessors = new ComFasterxmlVersionAccessors(providers, config);
        private final ComIbmVersionAccessors vaccForComIbmVersionAccessors = new ComIbmVersionAccessors(providers, config);
        private final ComMchangeVersionAccessors vaccForComMchangeVersionAccessors = new ComMchangeVersionAccessors(providers, config);
        private final ComNqzeroVersionAccessors vaccForComNqzeroVersionAccessors = new ComNqzeroVersionAccessors(providers, config);
        private final ComOracleVersionAccessors vaccForComOracleVersionAccessors = new ComOracleVersionAccessors(providers, config);
        private final ComTeradataVersionAccessors vaccForComTeradataVersionAccessors = new ComTeradataVersionAccessors(providers, config);
        private final ComUnboundidVersionAccessors vaccForComUnboundidVersionAccessors = new ComUnboundidVersionAccessors(providers, config);
        private final ComVaadinVersionAccessors vaccForComVaadinVersionAccessors = new ComVaadinVersionAccessors(providers, config);
        public ComVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.alibaba</b>
         */
        public ComAlibabaVersionAccessors getAlibaba() {
            return vaccForComAlibabaVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.beust</b>
         */
        public ComBeustVersionAccessors getBeust() {
            return vaccForComBeustVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.caucho</b>
         */
        public ComCauchoVersionAccessors getCaucho() {
            return vaccForComCauchoVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.fasterxml</b>
         */
        public ComFasterxmlVersionAccessors getFasterxml() {
            return vaccForComFasterxmlVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.ibm</b>
         */
        public ComIbmVersionAccessors getIbm() {
            return vaccForComIbmVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.mchange</b>
         */
        public ComMchangeVersionAccessors getMchange() {
            return vaccForComMchangeVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.nqzero</b>
         */
        public ComNqzeroVersionAccessors getNqzero() {
            return vaccForComNqzeroVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.oracle</b>
         */
        public ComOracleVersionAccessors getOracle() {
            return vaccForComOracleVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.teradata</b>
         */
        public ComTeradataVersionAccessors getTeradata() {
            return vaccForComTeradataVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.unboundid</b>
         */
        public ComUnboundidVersionAccessors getUnboundid() {
            return vaccForComUnboundidVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.com.vaadin</b>
         */
        public ComVaadinVersionAccessors getVaadin() {
            return vaccForComVaadinVersionAccessors;
        }

    }

    public static class ComAlibabaVersionAccessors extends VersionFactory  {

        private final ComAlibabaFastjson2VersionAccessors vaccForComAlibabaFastjson2VersionAccessors = new ComAlibabaFastjson2VersionAccessors(providers, config);
        public ComAlibabaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.alibaba.fastjson</b> with value <b>1.2.83</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getFastjson() { return getVersion("com.alibaba.fastjson"); }

        /**
         * Group of versions at <b>versions.com.alibaba.fastjson2</b>
         */
        public ComAlibabaFastjson2VersionAccessors getFastjson2() {
            return vaccForComAlibabaFastjson2VersionAccessors;
        }

    }

    public static class ComAlibabaFastjson2VersionAccessors extends VersionFactory  {

        public ComAlibabaFastjson2VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.alibaba.fastjson2.fastjson2</b> with value <b>2.0.26</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getFastjson2() { return getVersion("com.alibaba.fastjson2.fastjson2"); }

    }

    public static class ComBeustVersionAccessors extends VersionFactory  {

        public ComBeustVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.beust.jcommander</b> with value <b>1.78</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJcommander() { return getVersion("com.beust.jcommander"); }

    }

    public static class ComCauchoVersionAccessors extends VersionFactory  {

        public ComCauchoVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.caucho.resin</b> with value <b>4.0.65</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getResin() { return getVersion("com.caucho.resin"); }

    }

    public static class ComFasterxmlVersionAccessors extends VersionFactory  {

        private final ComFasterxmlJacksonVersionAccessors vaccForComFasterxmlJacksonVersionAccessors = new ComFasterxmlJacksonVersionAccessors(providers, config);
        public ComFasterxmlVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.fasterxml.jackson</b>
         */
        public ComFasterxmlJacksonVersionAccessors getJackson() {
            return vaccForComFasterxmlJacksonVersionAccessors;
        }

    }

    public static class ComFasterxmlJacksonVersionAccessors extends VersionFactory  {

        private final ComFasterxmlJacksonCoreVersionAccessors vaccForComFasterxmlJacksonCoreVersionAccessors = new ComFasterxmlJacksonCoreVersionAccessors(providers, config);
        public ComFasterxmlJacksonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.fasterxml.jackson.core</b>
         */
        public ComFasterxmlJacksonCoreVersionAccessors getCore() {
            return vaccForComFasterxmlJacksonCoreVersionAccessors;
        }

    }

    public static class ComFasterxmlJacksonCoreVersionAccessors extends VersionFactory  {

        private final ComFasterxmlJacksonCoreJacksonVersionAccessors vaccForComFasterxmlJacksonCoreJacksonVersionAccessors = new ComFasterxmlJacksonCoreJacksonVersionAccessors(providers, config);
        public ComFasterxmlJacksonCoreVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.fasterxml.jackson.core.jackson</b>
         */
        public ComFasterxmlJacksonCoreJacksonVersionAccessors getJackson() {
            return vaccForComFasterxmlJacksonCoreJacksonVersionAccessors;
        }

    }

    public static class ComFasterxmlJacksonCoreJacksonVersionAccessors extends VersionFactory  {

        public ComFasterxmlJacksonCoreJacksonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.fasterxml.jackson.core.jackson.databind</b> with value <b>2.11.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDatabind() { return getVersion("com.fasterxml.jackson.core.jackson.databind"); }

    }

    public static class ComIbmVersionAccessors extends VersionFactory  {

        private final ComIbmWebsphereVersionAccessors vaccForComIbmWebsphereVersionAccessors = new ComIbmWebsphereVersionAccessors(providers, config);
        public ComIbmVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.ibm.websphere</b>
         */
        public ComIbmWebsphereVersionAccessors getWebsphere() {
            return vaccForComIbmWebsphereVersionAccessors;
        }

    }

    public static class ComIbmWebsphereVersionAccessors extends VersionFactory  {

        private final ComIbmWebsphereAppserverVersionAccessors vaccForComIbmWebsphereAppserverVersionAccessors = new ComIbmWebsphereAppserverVersionAccessors(providers, config);
        public ComIbmWebsphereVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.ibm.websphere.appserver</b>
         */
        public ComIbmWebsphereAppserverVersionAccessors getAppserver() {
            return vaccForComIbmWebsphereAppserverVersionAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverVersionAccessors extends VersionFactory  {

        private final ComIbmWebsphereAppserverApiVersionAccessors vaccForComIbmWebsphereAppserverApiVersionAccessors = new ComIbmWebsphereAppserverApiVersionAccessors(providers, config);
        public ComIbmWebsphereAppserverVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.ibm.websphere.appserver.api</b>
         */
        public ComIbmWebsphereAppserverApiVersionAccessors getApi() {
            return vaccForComIbmWebsphereAppserverApiVersionAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiVersionAccessors extends VersionFactory  {

        private final ComIbmWebsphereAppserverApiComVersionAccessors vaccForComIbmWebsphereAppserverApiComVersionAccessors = new ComIbmWebsphereAppserverApiComVersionAccessors(providers, config);
        public ComIbmWebsphereAppserverApiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.ibm.websphere.appserver.api.com</b>
         */
        public ComIbmWebsphereAppserverApiComVersionAccessors getCom() {
            return vaccForComIbmWebsphereAppserverApiComVersionAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComVersionAccessors extends VersionFactory  {

        private final ComIbmWebsphereAppserverApiComIbmVersionAccessors vaccForComIbmWebsphereAppserverApiComIbmVersionAccessors = new ComIbmWebsphereAppserverApiComIbmVersionAccessors(providers, config);
        public ComIbmWebsphereAppserverApiComVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.ibm.websphere.appserver.api.com.ibm</b>
         */
        public ComIbmWebsphereAppserverApiComIbmVersionAccessors getIbm() {
            return vaccForComIbmWebsphereAppserverApiComIbmVersionAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComIbmVersionAccessors extends VersionFactory  {

        private final ComIbmWebsphereAppserverApiComIbmWebsphereVersionAccessors vaccForComIbmWebsphereAppserverApiComIbmWebsphereVersionAccessors = new ComIbmWebsphereAppserverApiComIbmWebsphereVersionAccessors(providers, config);
        public ComIbmWebsphereAppserverApiComIbmVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.ibm.websphere.appserver.api.com.ibm.websphere</b>
         */
        public ComIbmWebsphereAppserverApiComIbmWebsphereVersionAccessors getWebsphere() {
            return vaccForComIbmWebsphereAppserverApiComIbmWebsphereVersionAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComIbmWebsphereVersionAccessors extends VersionFactory  {

        private final ComIbmWebsphereAppserverApiComIbmWebsphereAppserverVersionAccessors vaccForComIbmWebsphereAppserverApiComIbmWebsphereAppserverVersionAccessors = new ComIbmWebsphereAppserverApiComIbmWebsphereAppserverVersionAccessors(providers, config);
        public ComIbmWebsphereAppserverApiComIbmWebsphereVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.ibm.websphere.appserver.api.com.ibm.websphere.appserver</b>
         */
        public ComIbmWebsphereAppserverApiComIbmWebsphereAppserverVersionAccessors getAppserver() {
            return vaccForComIbmWebsphereAppserverApiComIbmWebsphereAppserverVersionAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComIbmWebsphereAppserverVersionAccessors extends VersionFactory  {

        private final ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiVersionAccessors vaccForComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiVersionAccessors = new ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiVersionAccessors(providers, config);
        public ComIbmWebsphereAppserverApiComIbmWebsphereAppserverVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.ibm.websphere.appserver.api.com.ibm.websphere.appserver.api</b>
         */
        public ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiVersionAccessors getApi() {
            return vaccForComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiVersionAccessors;
        }

    }

    public static class ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiVersionAccessors extends VersionFactory  {

        public ComIbmWebsphereAppserverApiComIbmWebsphereAppserverApiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.ibm.websphere.appserver.api.com.ibm.websphere.appserver.api.wsoc</b> with value <b>1.0.10</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getWsoc() { return getVersion("com.ibm.websphere.appserver.api.com.ibm.websphere.appserver.api.wsoc"); }

    }

    public static class ComMchangeVersionAccessors extends VersionFactory  {

        public ComMchangeVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.mchange.c3p0</b> with value <b>0.9.5.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getC3p0() { return getVersion("com.mchange.c3p0"); }

    }

    public static class ComNqzeroVersionAccessors extends VersionFactory  {

        private final ComNqzeroPermitVersionAccessors vaccForComNqzeroPermitVersionAccessors = new ComNqzeroPermitVersionAccessors(providers, config);
        public ComNqzeroVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.nqzero.permit</b>
         */
        public ComNqzeroPermitVersionAccessors getPermit() {
            return vaccForComNqzeroPermitVersionAccessors;
        }

    }

    public static class ComNqzeroPermitVersionAccessors extends VersionFactory  {

        public ComNqzeroPermitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.nqzero.permit.reflect</b> with value <b>0.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getReflect() { return getVersion("com.nqzero.permit.reflect"); }

    }

    public static class ComOracleVersionAccessors extends VersionFactory  {

        private final ComOracleWeblogicVersionAccessors vaccForComOracleWeblogicVersionAccessors = new ComOracleWeblogicVersionAccessors(providers, config);
        public ComOracleVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.oracle.weblogic</b>
         */
        public ComOracleWeblogicVersionAccessors getWeblogic() {
            return vaccForComOracleWeblogicVersionAccessors;
        }

    }

    public static class ComOracleWeblogicVersionAccessors extends VersionFactory  {

        private final ComOracleWeblogicWeblogicVersionAccessors vaccForComOracleWeblogicWeblogicVersionAccessors = new ComOracleWeblogicWeblogicVersionAccessors(providers, config);
        public ComOracleWeblogicVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.oracle.weblogic.weblogic</b>
         */
        public ComOracleWeblogicWeblogicVersionAccessors getWeblogic() {
            return vaccForComOracleWeblogicWeblogicVersionAccessors;
        }

    }

    public static class ComOracleWeblogicWeblogicVersionAccessors extends VersionFactory  {

        public ComOracleWeblogicWeblogicVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.oracle.weblogic.weblogic.server</b> with value <b>1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getServer() { return getVersion("com.oracle.weblogic.weblogic.server"); }

    }

    public static class ComTeradataVersionAccessors extends VersionFactory  {

        private final ComTeradataJdbcVersionAccessors vaccForComTeradataJdbcVersionAccessors = new ComTeradataJdbcVersionAccessors(providers, config);
        public ComTeradataVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.teradata.jdbc</b>
         */
        public ComTeradataJdbcVersionAccessors getJdbc() {
            return vaccForComTeradataJdbcVersionAccessors;
        }

    }

    public static class ComTeradataJdbcVersionAccessors extends VersionFactory  {

        public ComTeradataJdbcVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.teradata.jdbc.terajdbc</b> with value <b>20.00.00.06</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getTerajdbc() { return getVersion("com.teradata.jdbc.terajdbc"); }

    }

    public static class ComUnboundidVersionAccessors extends VersionFactory  {

        private final ComUnboundidUnboundidVersionAccessors vaccForComUnboundidUnboundidVersionAccessors = new ComUnboundidUnboundidVersionAccessors(providers, config);
        public ComUnboundidVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.unboundid.unboundid</b>
         */
        public ComUnboundidUnboundidVersionAccessors getUnboundid() {
            return vaccForComUnboundidUnboundidVersionAccessors;
        }

    }

    public static class ComUnboundidUnboundidVersionAccessors extends VersionFactory  {

        public ComUnboundidUnboundidVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.unboundid.unboundid.ldapsdk</b> with value <b>4.0.9</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLdapsdk() { return getVersion("com.unboundid.unboundid.ldapsdk"); }

    }

    public static class ComVaadinVersionAccessors extends VersionFactory  {

        private final ComVaadinVaadinVersionAccessors vaccForComVaadinVaadinVersionAccessors = new ComVaadinVaadinVersionAccessors(providers, config);
        public ComVaadinVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.com.vaadin.vaadin</b>
         */
        public ComVaadinVaadinVersionAccessors getVaadin() {
            return vaccForComVaadinVaadinVersionAccessors;
        }

    }

    public static class ComVaadinVaadinVersionAccessors extends VersionFactory  {

        public ComVaadinVaadinVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>com.vaadin.vaadin.server</b> with value <b>7.7.14</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getServer() { return getVersion("com.vaadin.vaadin.server"); }

    }

    public static class CommonsVersionAccessors extends VersionFactory  {

        private final CommonsBeanutilsVersionAccessors vaccForCommonsBeanutilsVersionAccessors = new CommonsBeanutilsVersionAccessors(providers, config);
        private final CommonsCliVersionAccessors vaccForCommonsCliVersionAccessors = new CommonsCliVersionAccessors(providers, config);
        private final CommonsCollectionsVersionAccessors vaccForCommonsCollectionsVersionAccessors = new CommonsCollectionsVersionAccessors(providers, config);
        public CommonsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.commons.beanutils</b>
         */
        public CommonsBeanutilsVersionAccessors getBeanutils() {
            return vaccForCommonsBeanutilsVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.commons.cli</b>
         */
        public CommonsCliVersionAccessors getCli() {
            return vaccForCommonsCliVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.commons.collections</b>
         */
        public CommonsCollectionsVersionAccessors getCollections() {
            return vaccForCommonsCollectionsVersionAccessors;
        }

    }

    public static class CommonsBeanutilsVersionAccessors extends VersionFactory  {

        private final CommonsBeanutilsCommonsVersionAccessors vaccForCommonsBeanutilsCommonsVersionAccessors = new CommonsBeanutilsCommonsVersionAccessors(providers, config);
        public CommonsBeanutilsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.commons.beanutils.commons</b>
         */
        public CommonsBeanutilsCommonsVersionAccessors getCommons() {
            return vaccForCommonsBeanutilsCommonsVersionAccessors;
        }

    }

    public static class CommonsBeanutilsCommonsVersionAccessors extends VersionFactory  {

        public CommonsBeanutilsCommonsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>commons.beanutils.commons.beanutils</b> with value <b>1.9.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getBeanutils() { return getVersion("commons.beanutils.commons.beanutils"); }

    }

    public static class CommonsCliVersionAccessors extends VersionFactory  {

        private final CommonsCliCommonsVersionAccessors vaccForCommonsCliCommonsVersionAccessors = new CommonsCliCommonsVersionAccessors(providers, config);
        public CommonsCliVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.commons.cli.commons</b>
         */
        public CommonsCliCommonsVersionAccessors getCommons() {
            return vaccForCommonsCliCommonsVersionAccessors;
        }

    }

    public static class CommonsCliCommonsVersionAccessors extends VersionFactory  {

        public CommonsCliCommonsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>commons.cli.commons.cli</b> with value <b>1.5.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCli() { return getVersion("commons.cli.commons.cli"); }

    }

    public static class CommonsCollectionsVersionAccessors extends VersionFactory  {

        private final CommonsCollectionsCommonsVersionAccessors vaccForCommonsCollectionsCommonsVersionAccessors = new CommonsCollectionsCommonsVersionAccessors(providers, config);
        public CommonsCollectionsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.commons.collections.commons</b>
         */
        public CommonsCollectionsCommonsVersionAccessors getCommons() {
            return vaccForCommonsCollectionsCommonsVersionAccessors;
        }

    }

    public static class CommonsCollectionsCommonsVersionAccessors extends VersionFactory  {

        public CommonsCollectionsCommonsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>commons.collections.commons.collections</b> with value <b>3.2.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCollections() { return getVersion("commons.collections.commons.collections"); }

    }

    public static class IoVersionAccessors extends VersionFactory  {

        private final IoProjectreactorVersionAccessors vaccForIoProjectreactorVersionAccessors = new IoProjectreactorVersionAccessors(providers, config);
        private final IoUndertowVersionAccessors vaccForIoUndertowVersionAccessors = new IoUndertowVersionAccessors(providers, config);
        public IoVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.io.projectreactor</b>
         */
        public IoProjectreactorVersionAccessors getProjectreactor() {
            return vaccForIoProjectreactorVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.io.undertow</b>
         */
        public IoUndertowVersionAccessors getUndertow() {
            return vaccForIoUndertowVersionAccessors;
        }

    }

    public static class IoProjectreactorVersionAccessors extends VersionFactory  {

        private final IoProjectreactorReactorVersionAccessors vaccForIoProjectreactorReactorVersionAccessors = new IoProjectreactorReactorVersionAccessors(providers, config);
        public IoProjectreactorVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.io.projectreactor.reactor</b>
         */
        public IoProjectreactorReactorVersionAccessors getReactor() {
            return vaccForIoProjectreactorReactorVersionAccessors;
        }

    }

    public static class IoProjectreactorReactorVersionAccessors extends VersionFactory  {

        public IoProjectreactorReactorVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>io.projectreactor.reactor.core</b> with value <b>3.4.26</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("io.projectreactor.reactor.core"); }

    }

    public static class IoUndertowVersionAccessors extends VersionFactory  {

        private final IoUndertowUndertowVersionAccessors vaccForIoUndertowUndertowVersionAccessors = new IoUndertowUndertowVersionAccessors(providers, config);
        public IoUndertowVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.io.undertow.undertow</b>
         */
        public IoUndertowUndertowVersionAccessors getUndertow() {
            return vaccForIoUndertowUndertowVersionAccessors;
        }

    }

    public static class IoUndertowUndertowVersionAccessors extends VersionFactory  {

        public IoUndertowUndertowVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>io.undertow.undertow.core</b> with value <b>2.2.2.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("io.undertow.undertow.core"); }

        /**
         * Version alias <b>io.undertow.undertow.servlet</b> with value <b>2.2.2.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getServlet() { return getVersion("io.undertow.undertow.servlet"); }

    }

    public static class JavaxVersionAccessors extends VersionFactory  {

        private final JavaxMediaVersionAccessors vaccForJavaxMediaVersionAccessors = new JavaxMediaVersionAccessors(providers, config);
        private final JavaxServletVersionAccessors vaccForJavaxServletVersionAccessors = new JavaxServletVersionAccessors(providers, config);
        private final JavaxWebsocketVersionAccessors vaccForJavaxWebsocketVersionAccessors = new JavaxWebsocketVersionAccessors(providers, config);
        public JavaxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.media</b>
         */
        public JavaxMediaVersionAccessors getMedia() {
            return vaccForJavaxMediaVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.javax.servlet</b>
         */
        public JavaxServletVersionAccessors getServlet() {
            return vaccForJavaxServletVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.javax.websocket</b>
         */
        public JavaxWebsocketVersionAccessors getWebsocket() {
            return vaccForJavaxWebsocketVersionAccessors;
        }

    }

    public static class JavaxMediaVersionAccessors extends VersionFactory  {

        private final JavaxMediaJaiVersionAccessors vaccForJavaxMediaJaiVersionAccessors = new JavaxMediaJaiVersionAccessors(providers, config);
        public JavaxMediaVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.media.jai</b>
         */
        public JavaxMediaJaiVersionAccessors getJai() {
            return vaccForJavaxMediaJaiVersionAccessors;
        }

    }

    public static class JavaxMediaJaiVersionAccessors extends VersionFactory  {

        private final JavaxMediaJaiJaiVersionAccessors vaccForJavaxMediaJaiJaiVersionAccessors = new JavaxMediaJaiJaiVersionAccessors(providers, config);
        public JavaxMediaJaiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.media.jai.jai</b>
         */
        public JavaxMediaJaiJaiVersionAccessors getJai() {
            return vaccForJavaxMediaJaiJaiVersionAccessors;
        }

    }

    public static class JavaxMediaJaiJaiVersionAccessors extends VersionFactory  {

        public JavaxMediaJaiJaiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>javax.media.jai.jai.core</b> with value <b>1.1.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("javax.media.jai.jai.core"); }

    }

    public static class JavaxServletVersionAccessors extends VersionFactory  {

        private final JavaxServletJavaxVersionAccessors vaccForJavaxServletJavaxVersionAccessors = new JavaxServletJavaxVersionAccessors(providers, config);
        public JavaxServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.servlet.javax</b>
         */
        public JavaxServletJavaxVersionAccessors getJavax() {
            return vaccForJavaxServletJavaxVersionAccessors;
        }

    }

    public static class JavaxServletJavaxVersionAccessors extends VersionFactory  {

        private final JavaxServletJavaxServletVersionAccessors vaccForJavaxServletJavaxServletVersionAccessors = new JavaxServletJavaxServletVersionAccessors(providers, config);
        public JavaxServletJavaxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.servlet.javax.servlet</b>
         */
        public JavaxServletJavaxServletVersionAccessors getServlet() {
            return vaccForJavaxServletJavaxServletVersionAccessors;
        }

    }

    public static class JavaxServletJavaxServletVersionAccessors extends VersionFactory  {

        public JavaxServletJavaxServletVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>javax.servlet.javax.servlet.api</b> with value <b>4.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApi() { return getVersion("javax.servlet.javax.servlet.api"); }

    }

    public static class JavaxWebsocketVersionAccessors extends VersionFactory  {

        private final JavaxWebsocketJavaxVersionAccessors vaccForJavaxWebsocketJavaxVersionAccessors = new JavaxWebsocketJavaxVersionAccessors(providers, config);
        public JavaxWebsocketVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.websocket.javax</b>
         */
        public JavaxWebsocketJavaxVersionAccessors getJavax() {
            return vaccForJavaxWebsocketJavaxVersionAccessors;
        }

    }

    public static class JavaxWebsocketJavaxVersionAccessors extends VersionFactory  {

        private final JavaxWebsocketJavaxWebsocketVersionAccessors vaccForJavaxWebsocketJavaxWebsocketVersionAccessors = new JavaxWebsocketJavaxWebsocketVersionAccessors(providers, config);
        public JavaxWebsocketJavaxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.javax.websocket.javax.websocket</b>
         */
        public JavaxWebsocketJavaxWebsocketVersionAccessors getWebsocket() {
            return vaccForJavaxWebsocketJavaxWebsocketVersionAccessors;
        }

    }

    public static class JavaxWebsocketJavaxWebsocketVersionAccessors extends VersionFactory  {

        public JavaxWebsocketJavaxWebsocketVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>javax.websocket.javax.websocket.api</b> with value <b>1.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getApi() { return getVersion("javax.websocket.javax.websocket.api"); }

    }

    public static class JbossVersionAccessors extends VersionFactory  {

        private final JbossJbossVersionAccessors vaccForJbossJbossVersionAccessors = new JbossJbossVersionAccessors(providers, config);
        public JbossVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.jboss.jboss</b>
         */
        public JbossJbossVersionAccessors getJboss() {
            return vaccForJbossJbossVersionAccessors;
        }

    }

    public static class JbossJbossVersionAccessors extends VersionFactory  {

        public JbossJbossVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>jboss.jboss.serialization</b> with value <b>4.2.2.GA</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSerialization() { return getVersion("jboss.jboss.serialization"); }

    }

    public static class JunitVersionAccessors extends VersionFactory  {

        public JunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>junit.junit</b> with value <b>4.11</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJunit() { return getVersion("junit.junit"); }

    }

    public static class NetVersionAccessors extends VersionFactory  {

        private final NetJodahVersionAccessors vaccForNetJodahVersionAccessors = new NetJodahVersionAccessors(providers, config);
        private final NetSfVersionAccessors vaccForNetSfVersionAccessors = new NetSfVersionAccessors(providers, config);
        public NetVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.net.jodah</b>
         */
        public NetJodahVersionAccessors getJodah() {
            return vaccForNetJodahVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.net.sf</b>
         */
        public NetSfVersionAccessors getSf() {
            return vaccForNetSfVersionAccessors;
        }

    }

    public static class NetJodahVersionAccessors extends VersionFactory  {

        public NetJodahVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>net.jodah.expiringmap</b> with value <b>0.5.9</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getExpiringmap() { return getVersion("net.jodah.expiringmap"); }

    }

    public static class NetSfVersionAccessors extends VersionFactory  {

        private final NetSfJsonVersionAccessors vaccForNetSfJsonVersionAccessors = new NetSfJsonVersionAccessors(providers, config);
        public NetSfVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.net.sf.json</b>
         */
        public NetSfJsonVersionAccessors getJson() {
            return vaccForNetSfJsonVersionAccessors;
        }

    }

    public static class NetSfJsonVersionAccessors extends VersionFactory  {

        private final NetSfJsonLibVersionAccessors vaccForNetSfJsonLibVersionAccessors = new NetSfJsonLibVersionAccessors(providers, config);
        public NetSfJsonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.net.sf.json.lib</b>
         */
        public NetSfJsonLibVersionAccessors getLib() {
            return vaccForNetSfJsonLibVersionAccessors;
        }

    }

    public static class NetSfJsonLibVersionAccessors extends VersionFactory  {

        private final NetSfJsonLibJsonVersionAccessors vaccForNetSfJsonLibJsonVersionAccessors = new NetSfJsonLibJsonVersionAccessors(providers, config);
        public NetSfJsonLibVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.net.sf.json.lib.json</b>
         */
        public NetSfJsonLibJsonVersionAccessors getJson() {
            return vaccForNetSfJsonLibJsonVersionAccessors;
        }

    }

    public static class NetSfJsonLibJsonVersionAccessors extends VersionFactory  {

        public NetSfJsonLibJsonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>net.sf.json.lib.json.lib</b> with value <b>2.4</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLib() { return getVersion("net.sf.json.lib.json.lib"); }

    }

    public static class OrgVersionAccessors extends VersionFactory  {

        private final OrgApacheVersionAccessors vaccForOrgApacheVersionAccessors = new OrgApacheVersionAccessors(providers, config);
        private final OrgAspectjVersionAccessors vaccForOrgAspectjVersionAccessors = new OrgAspectjVersionAccessors(providers, config);
        private final OrgBeanshellVersionAccessors vaccForOrgBeanshellVersionAccessors = new OrgBeanshellVersionAccessors(providers, config);
        private final OrgClojureVersionAccessors vaccForOrgClojureVersionAccessors = new OrgClojureVersionAccessors(providers, config);
        private final OrgCodehausVersionAccessors vaccForOrgCodehausVersionAccessors = new OrgCodehausVersionAccessors(providers, config);
        private final OrgEclipseVersionAccessors vaccForOrgEclipseVersionAccessors = new OrgEclipseVersionAccessors(providers, config);
        private final OrgFusesourceVersionAccessors vaccForOrgFusesourceVersionAccessors = new OrgFusesourceVersionAccessors(providers, config);
        private final OrgGlassfishVersionAccessors vaccForOrgGlassfishVersionAccessors = new OrgGlassfishVersionAccessors(providers, config);
        private final OrgHibernateVersionAccessors vaccForOrgHibernateVersionAccessors = new OrgHibernateVersionAccessors(providers, config);
        private final OrgJavassistVersionAccessors vaccForOrgJavassistVersionAccessors = new OrgJavassistVersionAccessors(providers, config);
        private final OrgJbossVersionAccessors vaccForOrgJbossVersionAccessors = new OrgJbossVersionAccessors(providers, config);
        private final OrgJenkinsVersionAccessors vaccForOrgJenkinsVersionAccessors = new OrgJenkinsVersionAccessors(providers, config);
        private final OrgOw2VersionAccessors vaccForOrgOw2VersionAccessors = new OrgOw2VersionAccessors(providers, config);
        private final OrgPythonVersionAccessors vaccForOrgPythonVersionAccessors = new OrgPythonVersionAccessors(providers, config);
        private final OrgReflectionsVersionAccessors vaccForOrgReflectionsVersionAccessors = new OrgReflectionsVersionAccessors(providers, config);
        private final OrgSpringframeworkVersionAccessors vaccForOrgSpringframeworkVersionAccessors = new OrgSpringframeworkVersionAccessors(providers, config);
        public OrgVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache</b>
         */
        public OrgApacheVersionAccessors getApache() {
            return vaccForOrgApacheVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.aspectj</b>
         */
        public OrgAspectjVersionAccessors getAspectj() {
            return vaccForOrgAspectjVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.beanshell</b>
         */
        public OrgBeanshellVersionAccessors getBeanshell() {
            return vaccForOrgBeanshellVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.clojure</b>
         */
        public OrgClojureVersionAccessors getClojure() {
            return vaccForOrgClojureVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.codehaus</b>
         */
        public OrgCodehausVersionAccessors getCodehaus() {
            return vaccForOrgCodehausVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.eclipse</b>
         */
        public OrgEclipseVersionAccessors getEclipse() {
            return vaccForOrgEclipseVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.fusesource</b>
         */
        public OrgFusesourceVersionAccessors getFusesource() {
            return vaccForOrgFusesourceVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.glassfish</b>
         */
        public OrgGlassfishVersionAccessors getGlassfish() {
            return vaccForOrgGlassfishVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.hibernate</b>
         */
        public OrgHibernateVersionAccessors getHibernate() {
            return vaccForOrgHibernateVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.javassist</b>
         */
        public OrgJavassistVersionAccessors getJavassist() {
            return vaccForOrgJavassistVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.jboss</b>
         */
        public OrgJbossVersionAccessors getJboss() {
            return vaccForOrgJbossVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.jenkins</b>
         */
        public OrgJenkinsVersionAccessors getJenkins() {
            return vaccForOrgJenkinsVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.ow2</b>
         */
        public OrgOw2VersionAccessors getOw2() {
            return vaccForOrgOw2VersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.python</b>
         */
        public OrgPythonVersionAccessors getPython() {
            return vaccForOrgPythonVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.reflections</b>
         */
        public OrgReflectionsVersionAccessors getReflections() {
            return vaccForOrgReflectionsVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.springframework</b>
         */
        public OrgSpringframeworkVersionAccessors getSpringframework() {
            return vaccForOrgSpringframeworkVersionAccessors;
        }

    }

    public static class OrgApacheVersionAccessors extends VersionFactory  {

        private final OrgApacheClickVersionAccessors vaccForOrgApacheClickVersionAccessors = new OrgApacheClickVersionAccessors(providers, config);
        private final OrgApacheCommonsVersionAccessors vaccForOrgApacheCommonsVersionAccessors = new OrgApacheCommonsVersionAccessors(providers, config);
        private final OrgApacheLoggingVersionAccessors vaccForOrgApacheLoggingVersionAccessors = new OrgApacheLoggingVersionAccessors(providers, config);
        private final OrgApacheMavenVersionAccessors vaccForOrgApacheMavenVersionAccessors = new OrgApacheMavenVersionAccessors(providers, config);
        private final OrgApacheMyfacesVersionAccessors vaccForOrgApacheMyfacesVersionAccessors = new OrgApacheMyfacesVersionAccessors(providers, config);
        private final OrgApacheTomcatVersionAccessors vaccForOrgApacheTomcatVersionAccessors = new OrgApacheTomcatVersionAccessors(providers, config);
        private final OrgApacheWicketVersionAccessors vaccForOrgApacheWicketVersionAccessors = new OrgApacheWicketVersionAccessors(providers, config);
        public OrgApacheVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.click</b>
         */
        public OrgApacheClickVersionAccessors getClick() {
            return vaccForOrgApacheClickVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.apache.commons</b>
         */
        public OrgApacheCommonsVersionAccessors getCommons() {
            return vaccForOrgApacheCommonsVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.apache.logging</b>
         */
        public OrgApacheLoggingVersionAccessors getLogging() {
            return vaccForOrgApacheLoggingVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.apache.maven</b>
         */
        public OrgApacheMavenVersionAccessors getMaven() {
            return vaccForOrgApacheMavenVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.apache.myfaces</b>
         */
        public OrgApacheMyfacesVersionAccessors getMyfaces() {
            return vaccForOrgApacheMyfacesVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.apache.tomcat</b>
         */
        public OrgApacheTomcatVersionAccessors getTomcat() {
            return vaccForOrgApacheTomcatVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.apache.wicket</b>
         */
        public OrgApacheWicketVersionAccessors getWicket() {
            return vaccForOrgApacheWicketVersionAccessors;
        }

    }

    public static class OrgApacheClickVersionAccessors extends VersionFactory  {

        private final OrgApacheClickClickVersionAccessors vaccForOrgApacheClickClickVersionAccessors = new OrgApacheClickClickVersionAccessors(providers, config);
        public OrgApacheClickVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.click.click</b>
         */
        public OrgApacheClickClickVersionAccessors getClick() {
            return vaccForOrgApacheClickClickVersionAccessors;
        }

    }

    public static class OrgApacheClickClickVersionAccessors extends VersionFactory  {

        public OrgApacheClickClickVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.click.click.nodeps</b> with value <b>2.3.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNodeps() { return getVersion("org.apache.click.click.nodeps"); }

    }

    public static class OrgApacheCommonsVersionAccessors extends VersionFactory  {

        private final OrgApacheCommonsCommonsVersionAccessors vaccForOrgApacheCommonsCommonsVersionAccessors = new OrgApacheCommonsCommonsVersionAccessors(providers, config);
        public OrgApacheCommonsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.commons.commons</b>
         */
        public OrgApacheCommonsCommonsVersionAccessors getCommons() {
            return vaccForOrgApacheCommonsCommonsVersionAccessors;
        }

    }

    public static class OrgApacheCommonsCommonsVersionAccessors extends VersionFactory  {

        public OrgApacheCommonsCommonsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.commons.commons.collections4</b> with value <b>4.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCollections4() { return getVersion("org.apache.commons.commons.collections4"); }

        /**
         * Version alias <b>org.apache.commons.commons.lang3</b> with value <b>3.12.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLang3() { return getVersion("org.apache.commons.commons.lang3"); }

        /**
         * Version alias <b>org.apache.commons.commons.text</b> with value <b>1.8</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getText() { return getVersion("org.apache.commons.commons.text"); }

    }

    public static class OrgApacheLoggingVersionAccessors extends VersionFactory  {

        private final OrgApacheLoggingLog4jVersionAccessors vaccForOrgApacheLoggingLog4jVersionAccessors = new OrgApacheLoggingLog4jVersionAccessors(providers, config);
        public OrgApacheLoggingVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.logging.log4j</b>
         */
        public OrgApacheLoggingLog4jVersionAccessors getLog4j() {
            return vaccForOrgApacheLoggingLog4jVersionAccessors;
        }

    }

    public static class OrgApacheLoggingLog4jVersionAccessors extends VersionFactory  {

        private final OrgApacheLoggingLog4jLog4jVersionAccessors vaccForOrgApacheLoggingLog4jLog4jVersionAccessors = new OrgApacheLoggingLog4jLog4jVersionAccessors(providers, config);
        public OrgApacheLoggingLog4jVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.logging.log4j.log4j</b>
         */
        public OrgApacheLoggingLog4jLog4jVersionAccessors getLog4j() {
            return vaccForOrgApacheLoggingLog4jLog4jVersionAccessors;
        }

    }

    public static class OrgApacheLoggingLog4jLog4jVersionAccessors extends VersionFactory  {

        public OrgApacheLoggingLog4jLog4jVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.logging.log4j.log4j.core</b> with value <b>2.14.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("org.apache.logging.log4j.log4j.core"); }

    }

    public static class OrgApacheMavenVersionAccessors extends VersionFactory  {

        private final OrgApacheMavenPluginsVersionAccessors vaccForOrgApacheMavenPluginsVersionAccessors = new OrgApacheMavenPluginsVersionAccessors(providers, config);
        public OrgApacheMavenVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.maven.plugins</b>
         */
        public OrgApacheMavenPluginsVersionAccessors getPlugins() {
            return vaccForOrgApacheMavenPluginsVersionAccessors;
        }

    }

    public static class OrgApacheMavenPluginsVersionAccessors extends VersionFactory  {

        private final OrgApacheMavenPluginsMavenVersionAccessors vaccForOrgApacheMavenPluginsMavenVersionAccessors = new OrgApacheMavenPluginsMavenVersionAccessors(providers, config);
        public OrgApacheMavenPluginsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.maven.plugins.maven</b>
         */
        public OrgApacheMavenPluginsMavenVersionAccessors getMaven() {
            return vaccForOrgApacheMavenPluginsMavenVersionAccessors;
        }

    }

    public static class OrgApacheMavenPluginsMavenVersionAccessors extends VersionFactory  {

        private final OrgApacheMavenPluginsMavenAssemblyVersionAccessors vaccForOrgApacheMavenPluginsMavenAssemblyVersionAccessors = new OrgApacheMavenPluginsMavenAssemblyVersionAccessors(providers, config);
        public OrgApacheMavenPluginsMavenVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.maven.plugins.maven.assembly</b>
         */
        public OrgApacheMavenPluginsMavenAssemblyVersionAccessors getAssembly() {
            return vaccForOrgApacheMavenPluginsMavenAssemblyVersionAccessors;
        }

    }

    public static class OrgApacheMavenPluginsMavenAssemblyVersionAccessors extends VersionFactory  {

        public OrgApacheMavenPluginsMavenAssemblyVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.maven.plugins.maven.assembly.plugin</b> with value <b>3.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getPlugin() { return getVersion("org.apache.maven.plugins.maven.assembly.plugin"); }

    }

    public static class OrgApacheMyfacesVersionAccessors extends VersionFactory  {

        private final OrgApacheMyfacesCoreVersionAccessors vaccForOrgApacheMyfacesCoreVersionAccessors = new OrgApacheMyfacesCoreVersionAccessors(providers, config);
        public OrgApacheMyfacesVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.myfaces.core</b>
         */
        public OrgApacheMyfacesCoreVersionAccessors getCore() {
            return vaccForOrgApacheMyfacesCoreVersionAccessors;
        }

    }

    public static class OrgApacheMyfacesCoreVersionAccessors extends VersionFactory  {

        private final OrgApacheMyfacesCoreMyfacesVersionAccessors vaccForOrgApacheMyfacesCoreMyfacesVersionAccessors = new OrgApacheMyfacesCoreMyfacesVersionAccessors(providers, config);
        public OrgApacheMyfacesCoreVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.myfaces.core.myfaces</b>
         */
        public OrgApacheMyfacesCoreMyfacesVersionAccessors getMyfaces() {
            return vaccForOrgApacheMyfacesCoreMyfacesVersionAccessors;
        }

    }

    public static class OrgApacheMyfacesCoreMyfacesVersionAccessors extends VersionFactory  {

        public OrgApacheMyfacesCoreMyfacesVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.myfaces.core.myfaces.impl</b> with value <b>2.2.9</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getImpl() { return getVersion("org.apache.myfaces.core.myfaces.impl"); }

    }

    public static class OrgApacheTomcatVersionAccessors extends VersionFactory  {

        private final OrgApacheTomcatEmbedVersionAccessors vaccForOrgApacheTomcatEmbedVersionAccessors = new OrgApacheTomcatEmbedVersionAccessors(providers, config);
        private final OrgApacheTomcatTomcatVersionAccessors vaccForOrgApacheTomcatTomcatVersionAccessors = new OrgApacheTomcatTomcatVersionAccessors(providers, config);
        public OrgApacheTomcatVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.tomcat.embed</b>
         */
        public OrgApacheTomcatEmbedVersionAccessors getEmbed() {
            return vaccForOrgApacheTomcatEmbedVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.apache.tomcat.tomcat</b>
         */
        public OrgApacheTomcatTomcatVersionAccessors getTomcat() {
            return vaccForOrgApacheTomcatTomcatVersionAccessors;
        }

    }

    public static class OrgApacheTomcatEmbedVersionAccessors extends VersionFactory  {

        private final OrgApacheTomcatEmbedTomcatVersionAccessors vaccForOrgApacheTomcatEmbedTomcatVersionAccessors = new OrgApacheTomcatEmbedTomcatVersionAccessors(providers, config);
        public OrgApacheTomcatEmbedVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.tomcat.embed.tomcat</b>
         */
        public OrgApacheTomcatEmbedTomcatVersionAccessors getTomcat() {
            return vaccForOrgApacheTomcatEmbedTomcatVersionAccessors;
        }

    }

    public static class OrgApacheTomcatEmbedTomcatVersionAccessors extends VersionFactory  {

        private final OrgApacheTomcatEmbedTomcatEmbedVersionAccessors vaccForOrgApacheTomcatEmbedTomcatEmbedVersionAccessors = new OrgApacheTomcatEmbedTomcatEmbedVersionAccessors(providers, config);
        public OrgApacheTomcatEmbedTomcatVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.tomcat.embed.tomcat.embed</b>
         */
        public OrgApacheTomcatEmbedTomcatEmbedVersionAccessors getEmbed() {
            return vaccForOrgApacheTomcatEmbedTomcatEmbedVersionAccessors;
        }

    }

    public static class OrgApacheTomcatEmbedTomcatEmbedVersionAccessors extends VersionFactory  {

        public OrgApacheTomcatEmbedTomcatEmbedVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.tomcat.embed.tomcat.embed.core</b> with value <b>8.5.58</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("org.apache.tomcat.embed.tomcat.embed.core"); }

    }

    public static class OrgApacheTomcatTomcatVersionAccessors extends VersionFactory  {

        public OrgApacheTomcatTomcatVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.tomcat.tomcat.websocket</b> with value <b>9.0.62</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getWebsocket() { return getVersion("org.apache.tomcat.tomcat.websocket"); }

    }

    public static class OrgApacheWicketVersionAccessors extends VersionFactory  {

        private final OrgApacheWicketWicketVersionAccessors vaccForOrgApacheWicketWicketVersionAccessors = new OrgApacheWicketWicketVersionAccessors(providers, config);
        public OrgApacheWicketVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.apache.wicket.wicket</b>
         */
        public OrgApacheWicketWicketVersionAccessors getWicket() {
            return vaccForOrgApacheWicketWicketVersionAccessors;
        }

    }

    public static class OrgApacheWicketWicketVersionAccessors extends VersionFactory  {

        public OrgApacheWicketWicketVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.apache.wicket.wicket.util</b> with value <b>6.23.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getUtil() { return getVersion("org.apache.wicket.wicket.util"); }

    }

    public static class OrgAspectjVersionAccessors extends VersionFactory  {

        public OrgAspectjVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.aspectj.aspectjweaver</b> with value <b>1.9.7</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAspectjweaver() { return getVersion("org.aspectj.aspectjweaver"); }

    }

    public static class OrgBeanshellVersionAccessors extends VersionFactory  {

        public OrgBeanshellVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.beanshell.bsh</b> with value <b>2.0b5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getBsh() { return getVersion("org.beanshell.bsh"); }

    }

    public static class OrgClojureVersionAccessors extends VersionFactory  {

        public OrgClojureVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.clojure.clojure</b> with value <b>1.8.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getClojure() { return getVersion("org.clojure.clojure"); }

    }

    public static class OrgCodehausVersionAccessors extends VersionFactory  {

        private final OrgCodehausGroovyVersionAccessors vaccForOrgCodehausGroovyVersionAccessors = new OrgCodehausGroovyVersionAccessors(providers, config);
        public OrgCodehausVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.codehaus.groovy</b>
         */
        public OrgCodehausGroovyVersionAccessors getGroovy() {
            return vaccForOrgCodehausGroovyVersionAccessors;
        }

    }

    public static class OrgCodehausGroovyVersionAccessors extends VersionFactory  {

        public OrgCodehausGroovyVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.codehaus.groovy.groovy</b> with value <b>2.4.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getGroovy() { return getVersion("org.codehaus.groovy.groovy"); }

    }

    public static class OrgEclipseVersionAccessors extends VersionFactory  {

        private final OrgEclipseJettyVersionAccessors vaccForOrgEclipseJettyVersionAccessors = new OrgEclipseJettyVersionAccessors(providers, config);
        public OrgEclipseVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.eclipse.jetty</b>
         */
        public OrgEclipseJettyVersionAccessors getJetty() {
            return vaccForOrgEclipseJettyVersionAccessors;
        }

    }

    public static class OrgEclipseJettyVersionAccessors extends VersionFactory  {

        private final OrgEclipseJettyJettyVersionAccessors vaccForOrgEclipseJettyJettyVersionAccessors = new OrgEclipseJettyJettyVersionAccessors(providers, config);
        public OrgEclipseJettyVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.eclipse.jetty.jetty</b>
         */
        public OrgEclipseJettyJettyVersionAccessors getJetty() {
            return vaccForOrgEclipseJettyJettyVersionAccessors;
        }

    }

    public static class OrgEclipseJettyJettyVersionAccessors extends VersionFactory  {

        public OrgEclipseJettyJettyVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.eclipse.jetty.jetty.ant</b> with value <b>11.0.7</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAnt() { return getVersion("org.eclipse.jetty.jetty.ant"); }

    }

    public static class OrgFusesourceVersionAccessors extends VersionFactory  {

        private final OrgFusesourceJansiVersionAccessors vaccForOrgFusesourceJansiVersionAccessors = new OrgFusesourceJansiVersionAccessors(providers, config);
        public OrgFusesourceVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.fusesource.jansi</b>
         */
        public OrgFusesourceJansiVersionAccessors getJansi() {
            return vaccForOrgFusesourceJansiVersionAccessors;
        }

    }

    public static class OrgFusesourceJansiVersionAccessors extends VersionFactory  {

        public OrgFusesourceJansiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.fusesource.jansi.jansi</b> with value <b>2.4.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJansi() { return getVersion("org.fusesource.jansi.jansi"); }

    }

    public static class OrgGlassfishVersionAccessors extends VersionFactory  {

        private final OrgGlassfishTyrusVersionAccessors vaccForOrgGlassfishTyrusVersionAccessors = new OrgGlassfishTyrusVersionAccessors(providers, config);
        public OrgGlassfishVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.glassfish.tyrus</b>
         */
        public OrgGlassfishTyrusVersionAccessors getTyrus() {
            return vaccForOrgGlassfishTyrusVersionAccessors;
        }

    }

    public static class OrgGlassfishTyrusVersionAccessors extends VersionFactory  {

        private final OrgGlassfishTyrusTyrusVersionAccessors vaccForOrgGlassfishTyrusTyrusVersionAccessors = new OrgGlassfishTyrusTyrusVersionAccessors(providers, config);
        public OrgGlassfishTyrusVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.glassfish.tyrus.tyrus</b>
         */
        public OrgGlassfishTyrusTyrusVersionAccessors getTyrus() {
            return vaccForOrgGlassfishTyrusTyrusVersionAccessors;
        }

    }

    public static class OrgGlassfishTyrusTyrusVersionAccessors extends VersionFactory  {

        public OrgGlassfishTyrusTyrusVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.glassfish.tyrus.tyrus.server</b> with value <b>2.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getServer() { return getVersion("org.glassfish.tyrus.tyrus.server"); }

    }

    public static class OrgHibernateVersionAccessors extends VersionFactory  {

        private final OrgHibernateHibernateVersionAccessors vaccForOrgHibernateHibernateVersionAccessors = new OrgHibernateHibernateVersionAccessors(providers, config);
        public OrgHibernateVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.hibernate.hibernate</b>
         */
        public OrgHibernateHibernateVersionAccessors getHibernate() {
            return vaccForOrgHibernateHibernateVersionAccessors;
        }

    }

    public static class OrgHibernateHibernateVersionAccessors extends VersionFactory  {

        public OrgHibernateHibernateVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.hibernate.hibernate.core</b> with value <b>4.3.11.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("org.hibernate.hibernate.core"); }

    }

    public static class OrgJavassistVersionAccessors extends VersionFactory  {

        public OrgJavassistVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.javassist.javassist</b> with value <b>3.29.2-GA</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJavassist() { return getVersion("org.javassist.javassist"); }

    }

    public static class OrgJbossVersionAccessors extends VersionFactory  {

        private final OrgJbossInterceptorVersionAccessors vaccForOrgJbossInterceptorVersionAccessors = new OrgJbossInterceptorVersionAccessors(providers, config);
        private final OrgJbossRemotingVersionAccessors vaccForOrgJbossRemotingVersionAccessors = new OrgJbossRemotingVersionAccessors(providers, config);
        private final OrgJbossRemotingjmxVersionAccessors vaccForOrgJbossRemotingjmxVersionAccessors = new OrgJbossRemotingjmxVersionAccessors(providers, config);
        private final OrgJbossSpecVersionAccessors vaccForOrgJbossSpecVersionAccessors = new OrgJbossSpecVersionAccessors(providers, config);
        private final OrgJbossWeldVersionAccessors vaccForOrgJbossWeldVersionAccessors = new OrgJbossWeldVersionAccessors(providers, config);
        public OrgJbossVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.interceptor</b>
         */
        public OrgJbossInterceptorVersionAccessors getInterceptor() {
            return vaccForOrgJbossInterceptorVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.jboss.remoting</b>
         */
        public OrgJbossRemotingVersionAccessors getRemoting() {
            return vaccForOrgJbossRemotingVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.jboss.remotingjmx</b>
         */
        public OrgJbossRemotingjmxVersionAccessors getRemotingjmx() {
            return vaccForOrgJbossRemotingjmxVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.jboss.spec</b>
         */
        public OrgJbossSpecVersionAccessors getSpec() {
            return vaccForOrgJbossSpecVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.org.jboss.weld</b>
         */
        public OrgJbossWeldVersionAccessors getWeld() {
            return vaccForOrgJbossWeldVersionAccessors;
        }

    }

    public static class OrgJbossInterceptorVersionAccessors extends VersionFactory  {

        private final OrgJbossInterceptorJbossVersionAccessors vaccForOrgJbossInterceptorJbossVersionAccessors = new OrgJbossInterceptorJbossVersionAccessors(providers, config);
        public OrgJbossInterceptorVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.interceptor.jboss</b>
         */
        public OrgJbossInterceptorJbossVersionAccessors getJboss() {
            return vaccForOrgJbossInterceptorJbossVersionAccessors;
        }

    }

    public static class OrgJbossInterceptorJbossVersionAccessors extends VersionFactory  {

        private final OrgJbossInterceptorJbossInterceptorVersionAccessors vaccForOrgJbossInterceptorJbossInterceptorVersionAccessors = new OrgJbossInterceptorJbossInterceptorVersionAccessors(providers, config);
        public OrgJbossInterceptorJbossVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.interceptor.jboss.interceptor</b>
         */
        public OrgJbossInterceptorJbossInterceptorVersionAccessors getInterceptor() {
            return vaccForOrgJbossInterceptorJbossInterceptorVersionAccessors;
        }

    }

    public static class OrgJbossInterceptorJbossInterceptorVersionAccessors extends VersionFactory  {

        public OrgJbossInterceptorJbossInterceptorVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.jboss.interceptor.jboss.interceptor.core</b> with value <b>2.0.0.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("org.jboss.interceptor.jboss.interceptor.core"); }

        /**
         * Version alias <b>org.jboss.interceptor.jboss.interceptor.spi</b> with value <b>2.0.0.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSpi() { return getVersion("org.jboss.interceptor.jboss.interceptor.spi"); }

    }

    public static class OrgJbossRemotingVersionAccessors extends VersionFactory  {

        private final OrgJbossRemotingJbossVersionAccessors vaccForOrgJbossRemotingJbossVersionAccessors = new OrgJbossRemotingJbossVersionAccessors(providers, config);
        public OrgJbossRemotingVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.remoting.jboss</b>
         */
        public OrgJbossRemotingJbossVersionAccessors getJboss() {
            return vaccForOrgJbossRemotingJbossVersionAccessors;
        }

    }

    public static class OrgJbossRemotingJbossVersionAccessors extends VersionFactory  {

        public OrgJbossRemotingJbossVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.jboss.remoting.jboss.remoting</b> with value <b>4.0.19.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRemoting() { return getVersion("org.jboss.remoting.jboss.remoting"); }

    }

    public static class OrgJbossRemotingjmxVersionAccessors extends VersionFactory  {

        private final OrgJbossRemotingjmxRemotingVersionAccessors vaccForOrgJbossRemotingjmxRemotingVersionAccessors = new OrgJbossRemotingjmxRemotingVersionAccessors(providers, config);
        public OrgJbossRemotingjmxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.remotingjmx.remoting</b>
         */
        public OrgJbossRemotingjmxRemotingVersionAccessors getRemoting() {
            return vaccForOrgJbossRemotingjmxRemotingVersionAccessors;
        }

    }

    public static class OrgJbossRemotingjmxRemotingVersionAccessors extends VersionFactory  {

        public OrgJbossRemotingjmxRemotingVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.jboss.remotingjmx.remoting.jmx</b> with value <b>2.0.1.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJmx() { return getVersion("org.jboss.remotingjmx.remoting.jmx"); }

    }

    public static class OrgJbossSpecVersionAccessors extends VersionFactory  {

        private final OrgJbossSpecJavaxVersionAccessors vaccForOrgJbossSpecJavaxVersionAccessors = new OrgJbossSpecJavaxVersionAccessors(providers, config);
        public OrgJbossSpecVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.spec.javax</b>
         */
        public OrgJbossSpecJavaxVersionAccessors getJavax() {
            return vaccForOrgJbossSpecJavaxVersionAccessors;
        }

    }

    public static class OrgJbossSpecJavaxVersionAccessors extends VersionFactory  {

        private final OrgJbossSpecJavaxSecurityVersionAccessors vaccForOrgJbossSpecJavaxSecurityVersionAccessors = new OrgJbossSpecJavaxSecurityVersionAccessors(providers, config);
        public OrgJbossSpecJavaxVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.spec.javax.security</b>
         */
        public OrgJbossSpecJavaxSecurityVersionAccessors getSecurity() {
            return vaccForOrgJbossSpecJavaxSecurityVersionAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityVersionAccessors extends VersionFactory  {

        private final OrgJbossSpecJavaxSecurityJaccVersionAccessors vaccForOrgJbossSpecJavaxSecurityJaccVersionAccessors = new OrgJbossSpecJavaxSecurityJaccVersionAccessors(providers, config);
        public OrgJbossSpecJavaxSecurityVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.spec.javax.security.jacc</b>
         */
        public OrgJbossSpecJavaxSecurityJaccVersionAccessors getJacc() {
            return vaccForOrgJbossSpecJavaxSecurityJaccVersionAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccVersionAccessors extends VersionFactory  {

        private final OrgJbossSpecJavaxSecurityJaccJbossVersionAccessors vaccForOrgJbossSpecJavaxSecurityJaccJbossVersionAccessors = new OrgJbossSpecJavaxSecurityJaccJbossVersionAccessors(providers, config);
        public OrgJbossSpecJavaxSecurityJaccVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.spec.javax.security.jacc.jboss</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossVersionAccessors getJboss() {
            return vaccForOrgJbossSpecJavaxSecurityJaccJbossVersionAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossVersionAccessors extends VersionFactory  {

        private final OrgJbossSpecJavaxSecurityJaccJbossJaccVersionAccessors vaccForOrgJbossSpecJavaxSecurityJaccJbossJaccVersionAccessors = new OrgJbossSpecJavaxSecurityJaccJbossJaccVersionAccessors(providers, config);
        public OrgJbossSpecJavaxSecurityJaccJbossVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.spec.javax.security.jacc.jboss.jacc</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossJaccVersionAccessors getJacc() {
            return vaccForOrgJbossSpecJavaxSecurityJaccJbossJaccVersionAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossJaccVersionAccessors extends VersionFactory  {

        private final OrgJbossSpecJavaxSecurityJaccJbossJaccApiVersionAccessors vaccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiVersionAccessors = new OrgJbossSpecJavaxSecurityJaccJbossJaccApiVersionAccessors(providers, config);
        public OrgJbossSpecJavaxSecurityJaccJbossJaccVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.spec.javax.security.jacc.jboss.jacc.api</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiVersionAccessors getApi() {
            return vaccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiVersionAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossJaccApiVersionAccessors extends VersionFactory  {

        private final OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1VersionAccessors vaccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiV1VersionAccessors = new OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1VersionAccessors(providers, config);
        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.spec.javax.security.jacc.jboss.jacc.api.v1</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1VersionAccessors getV1() {
            return vaccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiV1VersionAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1VersionAccessors extends VersionFactory  {

        private final OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4VersionAccessors vaccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4VersionAccessors = new OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4VersionAccessors(providers, config);
        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.spec.javax.security.jacc.jboss.jacc.api.v1.v4</b>
         */
        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4VersionAccessors getV4() {
            return vaccForOrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4VersionAccessors;
        }

    }

    public static class OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4VersionAccessors extends VersionFactory  {

        public OrgJbossSpecJavaxSecurityJaccJbossJaccApiV1V4VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.jboss.spec.javax.security.jacc.jboss.jacc.api.v1.v4.spec</b> with value <b>1.0.3.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSpec() { return getVersion("org.jboss.spec.javax.security.jacc.jboss.jacc.api.v1.v4.spec"); }

    }

    public static class OrgJbossWeldVersionAccessors extends VersionFactory  {

        private final OrgJbossWeldWeldVersionAccessors vaccForOrgJbossWeldWeldVersionAccessors = new OrgJbossWeldWeldVersionAccessors(providers, config);
        public OrgJbossWeldVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jboss.weld.weld</b>
         */
        public OrgJbossWeldWeldVersionAccessors getWeld() {
            return vaccForOrgJbossWeldWeldVersionAccessors;
        }

    }

    public static class OrgJbossWeldWeldVersionAccessors extends VersionFactory  {

        public OrgJbossWeldWeldVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.jboss.weld.weld.core</b> with value <b>1.1.33.Final</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("org.jboss.weld.weld.core"); }

    }

    public static class OrgJenkinsVersionAccessors extends VersionFactory  {

        private final OrgJenkinsCiVersionAccessors vaccForOrgJenkinsCiVersionAccessors = new OrgJenkinsCiVersionAccessors(providers, config);
        public OrgJenkinsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jenkins.ci</b>
         */
        public OrgJenkinsCiVersionAccessors getCi() {
            return vaccForOrgJenkinsCiVersionAccessors;
        }

    }

    public static class OrgJenkinsCiVersionAccessors extends VersionFactory  {

        private final OrgJenkinsCiMainVersionAccessors vaccForOrgJenkinsCiMainVersionAccessors = new OrgJenkinsCiMainVersionAccessors(providers, config);
        public OrgJenkinsCiVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.jenkins.ci.main</b>
         */
        public OrgJenkinsCiMainVersionAccessors getMain() {
            return vaccForOrgJenkinsCiMainVersionAccessors;
        }

    }

    public static class OrgJenkinsCiMainVersionAccessors extends VersionFactory  {

        public OrgJenkinsCiMainVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.jenkins.ci.main.remoting</b> with value <b>2.55</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRemoting() { return getVersion("org.jenkins.ci.main.remoting"); }

    }

    public static class OrgOw2VersionAccessors extends VersionFactory  {

        private final OrgOw2AsmVersionAccessors vaccForOrgOw2AsmVersionAccessors = new OrgOw2AsmVersionAccessors(providers, config);
        public OrgOw2VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.ow2.asm</b>
         */
        public OrgOw2AsmVersionAccessors getAsm() {
            return vaccForOrgOw2AsmVersionAccessors;
        }

    }

    public static class OrgOw2AsmVersionAccessors extends VersionFactory  {

        public OrgOw2AsmVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.ow2.asm.asm</b> with value <b>8.0.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAsm() { return getVersion("org.ow2.asm.asm"); }

    }

    public static class OrgPythonVersionAccessors extends VersionFactory  {

        private final OrgPythonJythonVersionAccessors vaccForOrgPythonJythonVersionAccessors = new OrgPythonJythonVersionAccessors(providers, config);
        public OrgPythonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.python.jython</b>
         */
        public OrgPythonJythonVersionAccessors getJython() {
            return vaccForOrgPythonJythonVersionAccessors;
        }

    }

    public static class OrgPythonJythonVersionAccessors extends VersionFactory  {

        public OrgPythonJythonVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.python.jython.standalone</b> with value <b>2.5.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getStandalone() { return getVersion("org.python.jython.standalone"); }

    }

    public static class OrgReflectionsVersionAccessors extends VersionFactory  {

        public OrgReflectionsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.reflections.reflections</b> with value <b>0.9.10</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getReflections() { return getVersion("org.reflections.reflections"); }

    }

    public static class OrgSpringframeworkVersionAccessors extends VersionFactory  {

        private final OrgSpringframeworkSpringVersionAccessors vaccForOrgSpringframeworkSpringVersionAccessors = new OrgSpringframeworkSpringVersionAccessors(providers, config);
        public OrgSpringframeworkVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.org.springframework.spring</b>
         */
        public OrgSpringframeworkSpringVersionAccessors getSpring() {
            return vaccForOrgSpringframeworkSpringVersionAccessors;
        }

    }

    public static class OrgSpringframeworkSpringVersionAccessors extends VersionFactory  {

        private final OrgSpringframeworkSpringContextVersionAccessors vaccForOrgSpringframeworkSpringContextVersionAccessors = new OrgSpringframeworkSpringContextVersionAccessors(providers, config);
        public OrgSpringframeworkSpringVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.springframework.spring.aop</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAop() { return getVersion("org.springframework.spring.aop"); }

        /**
         * Version alias <b>org.springframework.spring.beans</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getBeans() { return getVersion("org.springframework.spring.beans"); }

        /**
         * Version alias <b>org.springframework.spring.core</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCore() { return getVersion("org.springframework.spring.core"); }

        /**
         * Version alias <b>org.springframework.spring.jdbc</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJdbc() { return getVersion("org.springframework.spring.jdbc"); }

        /**
         * Version alias <b>org.springframework.spring.oxm</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getOxm() { return getVersion("org.springframework.spring.oxm"); }

        /**
         * Version alias <b>org.springframework.spring.test</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getTest() { return getVersion("org.springframework.spring.test"); }

        /**
         * Version alias <b>org.springframework.spring.tx</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getTx() { return getVersion("org.springframework.spring.tx"); }

        /**
         * Version alias <b>org.springframework.spring.web</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getWeb() { return getVersion("org.springframework.spring.web"); }

        /**
         * Version alias <b>org.springframework.spring.webmvc</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getWebmvc() { return getVersion("org.springframework.spring.webmvc"); }

        /**
         * Group of versions at <b>versions.org.springframework.spring.context</b>
         */
        public OrgSpringframeworkSpringContextVersionAccessors getContext() {
            return vaccForOrgSpringframeworkSpringContextVersionAccessors;
        }

    }

    public static class OrgSpringframeworkSpringContextVersionAccessors extends VersionFactory  {

        public OrgSpringframeworkSpringContextVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>org.springframework.spring.context.support</b> with value <b>5.2.3.RELEASE</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSupport() { return getVersion("org.springframework.spring.context.support"); }

    }

    public static class RhinoVersionAccessors extends VersionFactory  {

        public RhinoVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>rhino.js</b> with value <b>1.7R2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getJs() { return getVersion("rhino.js"); }

    }

    public static class RomeVersionAccessors extends VersionFactory  {

        public RomeVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>rome.rome</b> with value <b>1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getRome() { return getVersion("rome.rome"); }

    }

    public static class XercesVersionAccessors extends VersionFactory  {

        public XercesVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>xerces.xercesimpl</b> with value <b>2.12.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getXercesimpl() { return getVersion("xerces.xercesimpl"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

    }

}
