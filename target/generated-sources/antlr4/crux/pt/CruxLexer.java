// Generated from crux\pt\Crux.g4 by ANTLR 4.7.2
package crux.pt;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CruxLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		SemiColon=10, Integer=11, True=12, False=13, Identifier=14, WhiteSpaces=15, 
		Comment=16, Open_paren=17, Close_paren=18, Open_brace=19, Close_brace=20, 
		Open_bracket=21, Close_bracket=22, ADD=23, SUB=24, MUL=25, DIV=26, GE=27, 
		LE=28, Not_Equal=29, Equal=30, GT=31, LT=32, Assign=33, Comma=34;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"SemiColon", "Integer", "True", "False", "Identifier", "WhiteSpaces", 
			"Comment", "Open_paren", "Close_paren", "Open_brace", "Close_brace", 
			"Open_bracket", "Close_bracket", "ADD", "SUB", "MUL", "DIV", "GE", "LE", 
			"Not_Equal", "Equal", "GT", "LT", "Assign", "Comma"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'||'", "'&&'", "'!'", "'if'", "'else'", "'loop'", "'break'", "'continue'", 
			"'return'", "';'", null, "'true'", "'false'", null, null, null, "'('", 
			"')'", "'{'", "'}'", "'['", "']'", "'+'", "'-'", "'*'", "'/'", "'>='", 
			"'<='", "'!='", "'=='", "'>'", "'<'", "'='", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "SemiColon", 
			"Integer", "True", "False", "Identifier", "WhiteSpaces", "Comment", "Open_paren", 
			"Close_paren", "Open_brace", "Close_brace", "Open_bracket", "Close_bracket", 
			"ADD", "SUB", "MUL", "DIV", "GE", "LE", "Not_Equal", "Equal", "GT", "LT", 
			"Assign", "Comma"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CruxLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Crux.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2$\u00ca\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6"+
		"\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3"+
		"\f\7\fx\n\f\f\f\16\f{\13\f\5\f}\n\f\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\17\3\17\7\17\u008c\n\17\f\17\16\17\u008f\13\17\3\20"+
		"\6\20\u0092\n\20\r\20\16\20\u0093\3\20\3\20\3\21\3\21\3\21\3\21\7\21\u009c"+
		"\n\21\f\21\16\21\u009f\13\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3"+
		"\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\3"+
		"\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\37\3\37\3\37\3 \3 \3!\3"+
		"!\3\"\3\"\3#\3#\2\2$\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\37= ?!A\"C#E$\3\2\b\3\2\63;\3\2\62;\4\2C\\c|\6\2\62"+
		";C\\aac|\5\2\13\f\17\17\"\"\4\2\f\f\17\17\2\u00ce\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3"+
		"\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3"+
		"\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\3G\3\2\2\2\5J\3\2\2\2\7M\3\2\2"+
		"\2\tO\3\2\2\2\13R\3\2\2\2\rW\3\2\2\2\17\\\3\2\2\2\21b\3\2\2\2\23k\3\2"+
		"\2\2\25r\3\2\2\2\27|\3\2\2\2\31~\3\2\2\2\33\u0083\3\2\2\2\35\u0089\3\2"+
		"\2\2\37\u0091\3\2\2\2!\u0097\3\2\2\2#\u00a2\3\2\2\2%\u00a4\3\2\2\2\'\u00a6"+
		"\3\2\2\2)\u00a8\3\2\2\2+\u00aa\3\2\2\2-\u00ac\3\2\2\2/\u00ae\3\2\2\2\61"+
		"\u00b0\3\2\2\2\63\u00b2\3\2\2\2\65\u00b4\3\2\2\2\67\u00b6\3\2\2\29\u00b9"+
		"\3\2\2\2;\u00bc\3\2\2\2=\u00bf\3\2\2\2?\u00c2\3\2\2\2A\u00c4\3\2\2\2C"+
		"\u00c6\3\2\2\2E\u00c8\3\2\2\2GH\7~\2\2HI\7~\2\2I\4\3\2\2\2JK\7(\2\2KL"+
		"\7(\2\2L\6\3\2\2\2MN\7#\2\2N\b\3\2\2\2OP\7k\2\2PQ\7h\2\2Q\n\3\2\2\2RS"+
		"\7g\2\2ST\7n\2\2TU\7u\2\2UV\7g\2\2V\f\3\2\2\2WX\7n\2\2XY\7q\2\2YZ\7q\2"+
		"\2Z[\7r\2\2[\16\3\2\2\2\\]\7d\2\2]^\7t\2\2^_\7g\2\2_`\7c\2\2`a\7m\2\2"+
		"a\20\3\2\2\2bc\7e\2\2cd\7q\2\2de\7p\2\2ef\7v\2\2fg\7k\2\2gh\7p\2\2hi\7"+
		"w\2\2ij\7g\2\2j\22\3\2\2\2kl\7t\2\2lm\7g\2\2mn\7v\2\2no\7w\2\2op\7t\2"+
		"\2pq\7p\2\2q\24\3\2\2\2rs\7=\2\2s\26\3\2\2\2t}\7\62\2\2uy\t\2\2\2vx\t"+
		"\3\2\2wv\3\2\2\2x{\3\2\2\2yw\3\2\2\2yz\3\2\2\2z}\3\2\2\2{y\3\2\2\2|t\3"+
		"\2\2\2|u\3\2\2\2}\30\3\2\2\2~\177\7v\2\2\177\u0080\7t\2\2\u0080\u0081"+
		"\7w\2\2\u0081\u0082\7g\2\2\u0082\32\3\2\2\2\u0083\u0084\7h\2\2\u0084\u0085"+
		"\7c\2\2\u0085\u0086\7n\2\2\u0086\u0087\7u\2\2\u0087\u0088\7g\2\2\u0088"+
		"\34\3\2\2\2\u0089\u008d\t\4\2\2\u008a\u008c\t\5\2\2\u008b\u008a\3\2\2"+
		"\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\36"+
		"\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0092\t\6\2\2\u0091\u0090\3\2\2\2\u0092"+
		"\u0093\3\2\2\2\u0093\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0095\3\2"+
		"\2\2\u0095\u0096\b\20\2\2\u0096 \3\2\2\2\u0097\u0098\7\61\2\2\u0098\u0099"+
		"\7\61\2\2\u0099\u009d\3\2\2\2\u009a\u009c\n\7\2\2\u009b\u009a\3\2\2\2"+
		"\u009c\u009f\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u00a0"+
		"\3\2\2\2\u009f\u009d\3\2\2\2\u00a0\u00a1\b\21\2\2\u00a1\"\3\2\2\2\u00a2"+
		"\u00a3\7*\2\2\u00a3$\3\2\2\2\u00a4\u00a5\7+\2\2\u00a5&\3\2\2\2\u00a6\u00a7"+
		"\7}\2\2\u00a7(\3\2\2\2\u00a8\u00a9\7\177\2\2\u00a9*\3\2\2\2\u00aa\u00ab"+
		"\7]\2\2\u00ab,\3\2\2\2\u00ac\u00ad\7_\2\2\u00ad.\3\2\2\2\u00ae\u00af\7"+
		"-\2\2\u00af\60\3\2\2\2\u00b0\u00b1\7/\2\2\u00b1\62\3\2\2\2\u00b2\u00b3"+
		"\7,\2\2\u00b3\64\3\2\2\2\u00b4\u00b5\7\61\2\2\u00b5\66\3\2\2\2\u00b6\u00b7"+
		"\7@\2\2\u00b7\u00b8\7?\2\2\u00b88\3\2\2\2\u00b9\u00ba\7>\2\2\u00ba\u00bb"+
		"\7?\2\2\u00bb:\3\2\2\2\u00bc\u00bd\7#\2\2\u00bd\u00be\7?\2\2\u00be<\3"+
		"\2\2\2\u00bf\u00c0\7?\2\2\u00c0\u00c1\7?\2\2\u00c1>\3\2\2\2\u00c2\u00c3"+
		"\7@\2\2\u00c3@\3\2\2\2\u00c4\u00c5\7>\2\2\u00c5B\3\2\2\2\u00c6\u00c7\7"+
		"?\2\2\u00c7D\3\2\2\2\u00c8\u00c9\7.\2\2\u00c9F\3\2\2\2\b\2y|\u008d\u0093"+
		"\u009d\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}