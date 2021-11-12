package dml.gremlin.general;

import java.io.Serializable;

import org.apache.tinkerpop.gremlin.process.traversal.Scope;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal.Symbols;

public class Step implements Serializable {
	private final String operator;
	private final Class[] classes;
    private final Object[] arguments;
    
    public Step(final String operator, final Class[] classes, final Object[] arguments) {
    	this.operator = operator;
    	this.classes = classes;
    	this.arguments = arguments;
    }
    
    public String getOperator() {
    	return this.operator;
    }
    
    public Class getClass(int index) {
    	return this.classes[index];
    }
    
    public Class[] getClasses() {
    	return this.classes;
    }
    
    public Object getArgument(int index) {
    	return this.arguments[index];
    }
    
    public Object[] getArguments() {
    	return this.arguments;
    }
    
    public String toString() {
    	String op = this.operator;
    	String str = "";
    	for(int i = 0, len = classes.length; i < len; i++) {
    		str += classes[i] + "; ";
    	}
    	return "Step: " + op + " - [" + str + "]";
    }
    
    public static final class StepName {

        private StepName() {
            // static fields only
        }
        
        // Gremlin 原生
        public static final String map = "map";
        public static final String flatMap = "flatMap";
        public static final String id = "id";
        public static final String label = "label";
        public static final String identity = "identity";
        public static final String constant = "constant";
        public static final String V = "V";
        public static final String E = "E";
        public static final String to = "to";
        public static final String out = "out";
        public static final String in = "in";
        public static final String both = "both";
        public static final String toE = "toE";
        public static final String outE = "outE";
        public static final String inE = "inE";
        public static final String bothE = "bothE";
        public static final String toV = "toV";
        public static final String outV = "outV";
        public static final String inV = "inV";
        public static final String bothV = "bothV";
        public static final String otherV = "otherV";
        public static final String order = "order";
        public static final String properties = "properties";
        public static final String values = "values";
        public static final String propertyMap = "propertyMap";
        public static final String valueMap = "valueMap";
        public static final String elementMap = "elementMap";
        public static final String select = "select";
        public static final String key = "key";
        public static final String value = "value";
        public static final String path = "path";
        public static final String match = "match";
        public static final String math = "math";
        public static final String sack = "sack";
        public static final String loops = "loops";
        public static final String project = "project";
        public static final String unfold = "unfold";
        public static final String fold = "fold";
        public static final String count = "count";
        public static final String sum = "sum";
        public static final String max = "max";
        public static final String min = "min";
        public static final String mean = "mean";
        public static final String group = "group";
        public static final String groupCount = "groupCount";
        public static final String tree = "tree";
        public static final String addV = "addV";
        public static final String addE = "addE";
        public static final String from = "from";
        public static final String filter = "filter";
        public static final String or = "or";
        public static final String and = "and";
        public static final String inject = "inject";
        public static final String dedup = "dedup";
        public static final String where = "where";
        public static final String has = "has";
        public static final String hasNot = "hasNot";
        public static final String hasLabel = "hasLabel";
        public static final String hasId = "hasId";
        public static final String hasKey = "hasKey";
        public static final String hasValue = "hasValue";
        public static final String is = "is";
        public static final String not = "not";
        public static final String range = "range";
        public static final String limit = "limit";
        public static final String skip = "skip";
        public static final String tail = "tail";
        public static final String coin = "coin";
        public static final String io = "io";
        public static final String read = "read";
        public static final String write = "write";

        public static final String timeLimit = "timeLimit";
        public static final String simplePath = "simplePath";
        public static final String cyclicPath = "cyclicPath";
        public static final String sample = "sample";

        public static final String drop = "drop";

        public static final String sideEffect = "sideEffect";
        public static final String cap = "cap";
        public static final String property = "property";

        /**
         * @deprecated As of release 3.4.3, replaced by {@link Symbols#aggregate} with a {@link Scope#local}.
         */
        @Deprecated
        public static final String store = "store";
        public static final String aggregate = "aggregate";
        public static final String subgraph = "subgraph";
        public static final String barrier = "barrier";
        public static final String index = "index";
        public static final String local = "local";
        public static final String emit = "emit";
        public static final String repeat = "repeat";
        public static final String until = "until";
        public static final String branch = "branch";
        public static final String union = "union";
        public static final String coalesce = "coalesce";
        public static final String choose = "choose";
        public static final String optional = "optional";

        public static final String pageRank = "pageRank";
        public static final String peerPressure = "peerPressure";
        public static final String connectedComponent = "connectedComponent";
        public static final String shortestPath = "shortestPath";
        public static final String program = "program";

        public static final String by = "by";
        public static final String with = "with";
        public static final String times = "times";
        public static final String as = "as";
        public static final String option = "option";

        // Ex 新增
        public static final String g = "g";
		public static final String print = "print";

    }
}
