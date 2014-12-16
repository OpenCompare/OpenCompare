'use strict';var Kotlin = {};
(function() {
  function g(a, b) {
    if (null != a && null != b) {
      for (var c in b) {
        b.hasOwnProperty(c) && (a[c] = b[c]);
      }
    }
  }
  function h(a) {
    for (var b = 0;b < a.length;b++) {
      if (null != a[b] && null == a[b].$metadata$ || a[b].$metadata$.type === Kotlin.TYPE.CLASS) {
        return a[b];
      }
    }
    return null;
  }
  function e(a, b, c) {
    for (var f = 0;f < b.length;f++) {
      if (null == b[f] || null != b[f].$metadata$) {
        var d = c(b[f]), k;
        for (k in d) {
          d.hasOwnProperty(k) && (!a.hasOwnProperty(k) || a[k].$classIndex$ < d[k].$classIndex$) && (a[k] = d[k]);
        }
      }
    }
  }
  function d(a, b) {
    var c = {};
    c.baseClasses = null == a ? [] : Array.isArray(a) ? a : [a];
    c.baseClass = h(c.baseClasses);
    c.classIndex = Kotlin.newClassIndex();
    c.functions = {};
    c.properties = {};
    if (null != b) {
      for (var f in b) {
        if (b.hasOwnProperty(f)) {
          var d = b[f];
          d.$classIndex$ = c.classIndex;
          "function" === typeof d ? c.functions[f] = d : c.properties[f] = d;
        }
      }
    }
    e(c.functions, c.baseClasses, function(a) {
      return a.$metadata$.functions;
    });
    e(c.properties, c.baseClasses, function(a) {
      return a.$metadata$.properties;
    });
    return c;
  }
  function a() {
    var a = this.object_initializer$();
    Object.defineProperty(this, "object", {value:a});
    return a;
  }
  function b(a) {
    return "function" === typeof a ? a() : a;
  }
  function c(a, b) {
    if (null != a && null == a.$metadata$ || a.$metadata$.classIndex < b.$metadata$.classIndex) {
      return!1;
    }
    var f = a.$metadata$.baseClasses, d;
    for (d = 0;d < f.length;d++) {
      if (f[d] === b) {
        return!0;
      }
    }
    for (d = 0;d < f.length;d++) {
      if (c(f[d], b)) {
        return!0;
      }
    }
    return!1;
  }
  function f(a, b) {
    return function() {
      if (null !== b) {
        var c = b;
        b = null;
        c.call(a);
      }
      return a;
    };
  }
  function m(a) {
    var b = {};
    if (null == a) {
      return b;
    }
    for (var c in a) {
      a.hasOwnProperty(c) && ("function" === typeof a[c] ? a[c].type === Kotlin.TYPE.INIT_FUN ? (a[c].className = c, Object.defineProperty(b, c, {get:a[c], configurable:!0})) : b[c] = a[c] : Object.defineProperty(b, c, a[c]));
    }
    return b;
  }
  var l = function() {
    return function() {
    };
  };
  Kotlin.TYPE = {CLASS:"class", TRAIT:"trait", OBJECT:"object", INIT_FUN:"init fun"};
  Kotlin.classCount = 0;
  Kotlin.newClassIndex = function() {
    var a = Kotlin.classCount;
    Kotlin.classCount++;
    return a;
  };
  Kotlin.createClassNow = function(b, c, f, e) {
    null == c && (c = l());
    g(c, e);
    b = d(b, f);
    b.type = Kotlin.TYPE.CLASS;
    f = null !== b.baseClass ? Object.create(b.baseClass.prototype) : {};
    Object.defineProperties(f, b.properties);
    g(f, b.functions);
    f.constructor = c;
    null != b.baseClass && (c.baseInitializer = b.baseClass);
    c.$metadata$ = b;
    c.prototype = f;
    Object.defineProperty(c, "object", {get:a, configurable:!0});
    return c;
  };
  Kotlin.createObjectNow = function(a, b, c) {
    a = new (Kotlin.createClassNow(a, b, c));
    a.$metadata$ = {type:Kotlin.TYPE.OBJECT};
    return a;
  };
  Kotlin.createTraitNow = function(b, c, f) {
    var e = function() {
    };
    g(e, f);
    e.$metadata$ = d(b, c);
    e.$metadata$.type = Kotlin.TYPE.TRAIT;
    e.prototype = {};
    Object.defineProperties(e.prototype, e.$metadata$.properties);
    g(e.prototype, e.$metadata$.functions);
    Object.defineProperty(e, "object", {get:a, configurable:!0});
    return e;
  };
  Kotlin.createClass = function(a, c, f, d) {
    function e() {
      var k = Kotlin.createClassNow(b(a), c, f, d);
      Object.defineProperty(this, e.className, {value:k});
      return k;
    }
    e.type = Kotlin.TYPE.INIT_FUN;
    return e;
  };
  Kotlin.createTrait = function(a, c, f) {
    function d() {
      var e = Kotlin.createTraitNow(b(a), c, f);
      Object.defineProperty(this, d.className, {value:e});
      return e;
    }
    d.type = Kotlin.TYPE.INIT_FUN;
    return d;
  };
  Kotlin.createObject = function(a, c, f) {
    return Kotlin.createObjectNow(b(a), c, f);
  };
  Kotlin.callGetter = function(a, b, c) {
    return b.$metadata$.properties[c].get.call(a);
  };
  Kotlin.callSetter = function(a, b, c, f) {
    b.$metadata$.properties[c].set.call(a, f);
  };
  Kotlin.isType = function(a, b) {
    return null == a || null == b ? !1 : a instanceof b ? !0 : null != b && null == b.$metadata$ || b.$metadata$.type == Kotlin.TYPE.CLASS ? !1 : c(a.constructor, b);
  };
  Kotlin.modules = {};
  Kotlin.definePackage = function(a, b) {
    var c = m(b);
    return null === a ? {value:c} : {get:f(c, a)};
  };
  Kotlin.defineRootPackage = function(a, b) {
    var c = m(b);
    c.$initializer$ = null === a ? l() : a;
    return c;
  };
  Kotlin.defineModule = function(a, b) {
    if (a in Kotlin.modules) {
      throw Error("Module " + a + " is already defined");
    }
    b.$initializer$.call(b);
    Object.defineProperty(Kotlin.modules, a, {value:b});
  };
})();
(function() {
  function g(a) {
    return function() {
      throw new TypeError(void 0 !== a ? "Function " + a + " is abstract" : "Function is abstract");
    };
  }
  String.prototype.startsWith = function(a) {
    return 0 === this.indexOf(a);
  };
  String.prototype.endsWith = function(a) {
    return-1 !== this.indexOf(a, this.length - a.length);
  };
  String.prototype.contains = function(a) {
    return-1 !== this.indexOf(a);
  };
  Kotlin.equals = function(a, b) {
    return null == a ? null == b : Array.isArray(a) ? Kotlin.arrayEquals(a, b) : "object" == typeof a && void 0 !== a.equals_za3rmp$ ? a.equals_za3rmp$(b) : a === b;
  };
  Kotlin.toString = function(a) {
    return null == a ? "null" : Array.isArray(a) ? Kotlin.arrayToString(a) : a.toString();
  };
  Kotlin.arrayToString = function(a) {
    return "[" + a.join(", ") + "]";
  };
  Kotlin.intUpto = function(a, b) {
    return new Kotlin.NumberRange(a, b);
  };
  Kotlin.intDownto = function(a, b) {
    return new Kotlin.Progression(a, b, -1);
  };
  Kotlin.RuntimeException = Kotlin.createClassNow();
  Kotlin.NullPointerException = Kotlin.createClassNow();
  Kotlin.NoSuchElementException = Kotlin.createClassNow();
  Kotlin.IllegalArgumentException = Kotlin.createClassNow();
  Kotlin.IllegalStateException = Kotlin.createClassNow();
  Kotlin.UnsupportedOperationException = Kotlin.createClassNow();
  Kotlin.IOException = Kotlin.createClassNow();
  Kotlin.throwNPE = function() {
    throw new Kotlin.NullPointerException;
  };
  Kotlin.Iterator = Kotlin.createClassNow(null, null, {next:g("Iterator#next"), hasNext:g("Iterator#hasNext")});
  var h = Kotlin.createClassNow(Kotlin.Iterator, function(a) {
    this.array = a;
    this.index = 0;
  }, {next:function() {
    return this.array[this.index++];
  }, hasNext:function() {
    return this.index < this.array.length;
  }, remove:function() {
    if (0 > this.index || this.index > this.array.length) {
      throw new RangeError;
    }
    this.index--;
    this.array.splice(this.index, 1);
  }}), e = Kotlin.createClassNow(h, function(a) {
    this.list = a;
    this.size = a.size();
    this.index = 0;
  }, {next:function() {
    return this.list.get(this.index++);
  }});
  Kotlin.Collection = Kotlin.createClassNow();
  Kotlin.Enum = Kotlin.createClassNow(null, function() {
    this.ordinal$ = this.name$ = void 0;
  }, {name:function() {
    return this.name$;
  }, ordinal:function() {
    return this.ordinal$;
  }, toString:function() {
    return this.name();
  }});
  (function() {
    function a(a) {
      return this[a];
    }
    function b() {
      return this.values$;
    }
    Kotlin.createEnumEntries = function(c) {
      var f = 0, d = [], e;
      for (e in c) {
        if (c.hasOwnProperty(e)) {
          var g = c[e];
          d[f] = g;
          g.ordinal$ = f;
          g.name$ = e;
          f++;
        }
      }
      c.values$ = d;
      c.valueOf_61zpoe$ = a;
      c.values = b;
      return c;
    };
  })();
  Kotlin.PropertyMetadata = Kotlin.createClassNow(null, function(a) {
    this.name = a;
  });
  Kotlin.AbstractCollection = Kotlin.createClassNow(Kotlin.Collection, null, {addAll_xeylzf$:function(a) {
    var b = !1;
    for (a = a.iterator();a.hasNext();) {
      this.add_za3rmp$(a.next()) && (b = !0);
    }
    return b;
  }, removeAll_xeylzf$:function(a) {
    for (var b = !1, c = this.iterator();c.hasNext();) {
      a.contains_za3rmp$(c.next()) && (c.remove(), b = !0);
    }
    return b;
  }, retainAll_xeylzf$:function(a) {
    for (var b = !1, c = this.iterator();c.hasNext();) {
      a.contains_za3rmp$(c.next()) || (c.remove(), b = !0);
    }
    return b;
  }, containsAll_xeylzf$:function(a) {
    for (a = a.iterator();a.hasNext();) {
      if (!this.contains_za3rmp$(a.next())) {
        return!1;
      }
    }
    return!0;
  }, isEmpty:function() {
    return 0 === this.size();
  }, iterator:function() {
    return new h(this.toArray());
  }, equals_za3rmp$:function(a) {
    if (this.size() !== a.size()) {
      return!1;
    }
    var b = this.iterator();
    a = a.iterator();
    for (var c = this.size();0 < c--;) {
      if (!Kotlin.equals(b.next(), a.next())) {
        return!1;
      }
    }
    return!0;
  }, toString:function() {
    for (var a = "[", b = this.iterator(), c = !0, f = this.size();0 < f--;) {
      c ? c = !1 : a += ", ", a += b.next();
    }
    return a + "]";
  }, toJSON:function() {
    return this.toArray();
  }});
  Kotlin.AbstractList = Kotlin.createClassNow(Kotlin.AbstractCollection, null, {iterator:function() {
    return new e(this);
  }, remove_za3rmp$:function(a) {
    a = this.indexOf_za3rmp$(a);
    return-1 !== a ? (this.remove_za3lpa$(a), !0) : !1;
  }, contains_za3rmp$:function(a) {
    return-1 !== this.indexOf_za3rmp$(a);
  }});
  Kotlin.ArrayList = Kotlin.createClassNow(Kotlin.AbstractList, function() {
    this.array = [];
  }, {get_za3lpa$:function(a) {
    this.checkRange(a);
    return this.array[a];
  },get:function(a){return this.get_za3lpa$(a);}
   , set_vux3hl$:function(a, b) {
    this.checkRange(a);
    this.array[a] = b;
  }, size:function() {
    return this.array.length;
  }, iterator:function() {
    return Kotlin.arrayIterator(this.array);
  }, add_za3rmp$:function(a) {
    this.array.push(a);
    return!0;
  }, add_vux3hl$:function(a, b) {
    this.array.splice(a, 0, b);
  }, addAll_xeylzf$:function(a) {
    var b = a.iterator(), c = this.array.length;
    for (a = a.size();0 < a--;) {
      this.array[c++] = b.next();
    }
  }, remove_za3lpa$:function(a) {
    this.checkRange(a);
    return this.array.splice(a, 1)[0];
  }, clear:function() {
    this.array.length = 0;
  }, indexOf_za3rmp$:function(a) {
    for (var b = 0;b < this.array.length;b++) {
      if (Kotlin.equals(this.array[b], a)) {
        return b;
      }
    }
    return-1;
  }, lastIndexOf_za3rmp$:function(a) {
    for (var b = this.array.length - 1;0 <= b;b--) {
      if (Kotlin.equals(this.array[b], a)) {
        return b;
      }
    }
    return-1;
  }, toArray:function() {
    return this.array.slice(0);
  }, toString:function() {
    return "[" + this.array.join(", ") + "]";
  }, toJSON:function() {
    return this.array;
  }, checkRange:function(a) {
    if (0 > a || a >= this.array.length) {
      throw new RangeError;
    }
  }});
  Kotlin.Runnable = Kotlin.createClassNow(null, null, {run:g("Runnable#run")});
  Kotlin.Comparable = Kotlin.createClassNow(null, null, {compareTo:g("Comparable#compareTo")});
  Kotlin.Appendable = Kotlin.createClassNow(null, null, {append:g("Appendable#append")});
  Kotlin.Closeable = Kotlin.createClassNow(null, null, {close:g("Closeable#close")});
  Kotlin.safeParseInt = function(a) {
    a = parseInt(a, 10);
    return isNaN(a) ? null : a;
  };
  Kotlin.safeParseDouble = function(a) {
    a = parseFloat(a);
    return isNaN(a) ? null : a;
  };
  Kotlin.arrayEquals = function(a, b) {
    if (a === b) {
      return!0;
    }
    if (!Array.isArray(b) || a.length !== b.length) {
      return!1;
    }
    for (var c = 0, f = a.length;c < f;c++) {
      if (!Kotlin.equals(a[c], b[c])) {
        return!1;
      }
    }
    return!0;
  };
  Kotlin.System = function() {
    var a = "", b = function(b) {
      void 0 !== b && (a = null === b || "object" !== typeof b ? a + b : a + b.toString());
    }, c = function(b) {
      this.print(b);
      a += "\n";
    };
    return{out:function() {
      return{print:b, println:c};
    }, output:function() {
      return a;
    }, flush:function() {
      a = "";
    }};
  }();
  Kotlin.println = function(a) {
    Kotlin.System.out().println(a);
  };
  Kotlin.print = function(a) {
    Kotlin.System.out().print(a);
  };
  Kotlin.RangeIterator = Kotlin.createClassNow(Kotlin.Iterator, function(a, b, c) {
    this.start = a;
    this.end = b;
    this.increment = c;
    this.i = a;
  }, {next:function() {
    var a = this.i;
    this.i += this.increment;
    return a;
  }, hasNext:function() {
    return this.i <= this.end;
  }});
  Kotlin.NumberRange = Kotlin.createClassNow(null, function(a, b) {
    this.start = a;
    this.end = b;
    this.increment = 1;
  }, {contains:function(a) {
    return this.start <= a && a <= this.end;
  }, iterator:function() {
    return new Kotlin.RangeIterator(this.start, this.end);
  }});
  Kotlin.Progression = Kotlin.createClassNow(null, function(a, b, c) {
    this.start = a;
    this.end = b;
    this.increment = c;
  }, {iterator:function() {
    return new Kotlin.RangeIterator(this.start, this.end, this.increment);
  }});
  Kotlin.Comparator = Kotlin.createClassNow(null, null, {compare:g("Comparator#compare")});
  var d = Kotlin.createClassNow(Kotlin.Comparator, function(a) {
    this.compare = a;
  });
  Kotlin.comparator = function(a) {
    return new d(a);
  };
  Kotlin.collectionsMax = function(a, b) {
    if (a.isEmpty()) {
      throw Error();
    }
    for (var c = a.iterator(), f = c.next();c.hasNext();) {
      var d = c.next();
      0 > b.compare(f, d) && (f = d);
    }
    return f;
  };
  Kotlin.collectionsSort = function(a, b) {
    var c = void 0;
    void 0 !== b && (c = b.compare.bind(b));
    a instanceof Array && a.sort(c);
    for (var f = [], d = a.iterator();d.hasNext();) {
      f.push(d.next());
    }
    f.sort(c);
    c = 0;
    for (d = f.length;c < d;c++) {
      a.set_vux3hl$(c, f[c]);
    }
  };
  Kotlin.copyToArray = function(a) {
    var b = [];
    for (a = a.iterator();a.hasNext();) {
      b.push(a.next());
    }
    return b;
  };
  Kotlin.StringBuilder = Kotlin.createClassNow(null, function() {
    this.string = "";
  }, {append:function(a) {
    this.string += a.toString();
    return this;
  }, toString:function() {
    return this.string;
  }});
  Kotlin.splitString = function(a, b, c) {
    return a.split(RegExp(b), c);
  };
  Kotlin.nullArray = function(a) {
    for (var b = [];0 < a;) {
      b[--a] = null;
    }
    return b;
  };
  Kotlin.numberArrayOfSize = function(a) {
    return Kotlin.arrayFromFun(a, function() {
      return 0;
    });
  };
  Kotlin.charArrayOfSize = function(a) {
    return Kotlin.arrayFromFun(a, function() {
      return "\x00";
    });
  };
  Kotlin.booleanArrayOfSize = function(a) {
    return Kotlin.arrayFromFun(a, function() {
      return!1;
    });
  };
  Kotlin.arrayFromFun = function(a, b) {
    for (var c = Array(a), d = 0;d < a;d++) {
      c[d] = b(d);
    }
    return c;
  };
  Kotlin.arrayIndices = function(a) {
    return new Kotlin.NumberRange(0, a.length - 1);
  };
  Kotlin.arrayIterator = function(a) {
    return new h(a);
  };
  Kotlin.jsonFromTuples = function(a) {
    for (var b = a.length, c = {};0 < b;) {
      --b, c[a[b][0]] = a[b][1];
    }
    return c;
  };
  Kotlin.jsonAddProperties = function(a, b) {
    for (var c in b) {
      b.hasOwnProperty(c) && (a[c] = b[c]);
    }
    return a;
  };
})();
(function() {
  function g(a) {
    if ("string" == typeof a) {
      return a;
    }
    if ("function" == typeof a.hashCode) {
      return a = a.hashCode(), "string" == typeof a ? a : g(a);
    }
    if ("function" == typeof a.toString) {
      return a.toString();
    }
    try {
      return String(a);
    } catch (b) {
      return Object.prototype.toString.call(a);
    }
  }
  function h(a, b) {
    return a.equals(b);
  }
  function e(a, b) {
    return "function" == typeof b.equals ? b.equals(a) : a === b;
  }
  function d(a) {
    return function(b) {
      if (null === b) {
        throw Error("null is not a valid " + a);
      }
      if ("undefined" == typeof b) {
        throw Error(a + " must not be undefined");
      }
    };
  }
  function a(a, b, c, d) {
    this[0] = a;
    this.entries = [];
    this.addEntry(b, c);
    null !== d && (this.getEqualityFunction = function() {
      return d;
    });
  }
  function b(a) {
    return function(b) {
      for (var c = this.entries.length, d, f = this.getEqualityFunction(b);c--;) {
        if (d = this.entries[c], f(b, d[0])) {
          switch(a) {
            case n:
              return!0;
            case s:
              return d;
            case t:
              return[c, d[1]];
          }
        }
      }
      return!1;
    };
  }
  function c(a) {
    return function(b) {
      for (var c = b.length, d = 0, f = this.entries.length;d < f;++d) {
        b[c + d] = this.entries[d][a];
      }
    };
  }
  function f(b, c) {
    var d = b[c];
    return d && d instanceof a ? d : null;
  }
  var m = "function" == typeof Array.prototype.splice ? function(a, b) {
    a.splice(b, 1);
  } : function(a, b) {
    var c, d, f;
    if (b === a.length - 1) {
      a.length = b;
    } else {
      for (c = a.slice(b + 1), a.length = b, d = 0, f = c.length;d < f;++d) {
        a[b + d] = c[d];
      }
    }
  }, l = d("key"), r = d("value"), n = 0, s = 1, t = 2;
  a.prototype = {getEqualityFunction:function(a) {
    return "function" == typeof a.equals ? h : e;
  }, getEntryForKey:b(s), getEntryAndIndexForKey:b(t), removeEntryForKey:function(a) {
    return(a = this.getEntryAndIndexForKey(a)) ? (m(this.entries, a[0]), a[1]) : null;
  }, addEntry:function(a, b) {
    this.entries[this.entries.length] = [a, b];
  }, keys:c(0), values:c(1), getEntries:function(a) {
    for (var b = a.length, c = 0, d = this.entries.length;c < d;++c) {
      a[b + c] = this.entries[c].slice(0);
    }
  }, containsKey_za3rmp$:b(n), containsValue_za3rmp$:function(a) {
    for (var b = this.entries.length;b--;) {
      if (a === this.entries[b][1]) {
        return!0;
      }
    }
    return!1;
  }};
  var u = function(b, c) {
    var d = this, e = [], h = {}, p = "function" == typeof b ? b : g, n = "function" == typeof c ? c : null;
    this.put_wn2jw4$ = function(b, c) {
      l(b);
      r(c);
      var d = p(b), g, k = null;
      (g = f(h, d)) ? (d = g.getEntryForKey(b)) ? (k = d[1], d[1] = c) : g.addEntry(b, c) : (g = new a(d, b, c, n), e[e.length] = g, h[d] = g);
      return k;
    };
    this.get_za3rmp$ = function(a) {
      l(a);
      var b = p(a);
      if (b = f(h, b)) {
        if (a = b.getEntryForKey(a)) {
          return a[1];
        }
      }
      return null;
    };
    this.containsKey_za3rmp$ = function(a) {
      l(a);
      var b = p(a);
      return(b = f(h, b)) ? b.containsKey_za3rmp$(a) : !1;
    };
    this.containsValue_za3rmp$ = function(a) {
      r(a);
      for (var b = e.length;b--;) {
        if (e[b].containsValue_za3rmp$(a)) {
          return!0;
        }
      }
      return!1;
    };
    this.clear = function() {
      e.length = 0;
      h = {};
    };
    this.isEmpty = function() {
      return!e.length;
    };
    var q = function(a) {
      return function() {
        for (var b = [], c = e.length;c--;) {
          e[c][a](b);
        }
        return b;
      };
    };
    this._keys = q("keys");
    this._values = q("values");
    this._entries = q("getEntries");
    this.values = function() {
      for (var a = this._values(), b = a.length, c = new Kotlin.ArrayList;b--;) {
        c.add_za3rmp$(a[b]);
      }
      return c;
    };
    this.remove_za3rmp$ = function(a) {
      l(a);
      var b = p(a), c = null, d = f(h, b);
      if (d && (c = d.removeEntryForKey(a), null !== c && !d.entries.length)) {
        a: {
          for (a = e.length;a--;) {
            if (d = e[a], b === d[0]) {
              break a;
            }
          }
          a = null;
        }
        m(e, a);
        delete h[b];
      }
      return c;
    };
    this.size = function() {
      for (var a = 0, b = e.length;b--;) {
        a += e[b].entries.length;
      }
      return a;
    };
    this.each = function(a) {
      for (var b = d._entries(), c = b.length, e;c--;) {
        e = b[c], a(e[0], e[1]);
      }
    };
    this.putAll_za3j1t$ = function(a, b) {
      for (var c = a._entries(), e, f, g, h = c.length, k = "function" == typeof b;h--;) {
        e = c[h], f = e[0], e = e[1], k && (g = d.get(f)) && (e = b(f, g, e)), d.put_wn2jw4$(f, e);
      }
    };
    this.clone = function() {
      var a = new u(b, c);
      a.putAll_za3j1t$(d);
      return a;
    };
    this.keySet = function() {
      for (var a = new Kotlin.ComplexHashSet, b = this._keys(), c = b.length;c--;) {
        a.add_za3rmp$(b[c]);
      }
      return a;
    };
  };
  Kotlin.HashTable = u;
})();
Kotlin.Map = Kotlin.createClassNow();
Kotlin.HashMap = Kotlin.createClassNow(Kotlin.Map, function() {
  Kotlin.HashTable.call(this);
});
Kotlin.ComplexHashMap = Kotlin.HashMap;
(function() {
  var g = Kotlin.createClassNow(Kotlin.Iterator, function(e, d) {
    this.map = e;
    this.keys = d;
    this.size = d.length;
    this.index = 0;
  }, {next:function() {
    return this.map[this.keys[this.index++]];
  }, hasNext:function() {
    return this.index < this.size;
  }}), h = Kotlin.createClassNow(Kotlin.Collection, function(e) {
    this.map = e;
  }, {iterator:function() {
    return new g(this.map.map, Object.keys(this.map.map));
  }, isEmpty:function() {
    return 0 === this.map.$size;
  }, contains:function(e) {
    return this.map.containsValue_za3rmp$(e);
  }});
  Kotlin.PrimitiveHashMap = Kotlin.createClassNow(Kotlin.Map, function() {
    this.$size = 0;
    this.map = {};
  }, {size:function() {
    return this.$size;
  }, isEmpty:function() {
    return 0 === this.$size;
  }, containsKey_za3rmp$:function(e) {
    return void 0 !== this.map[e];
  }, containsValue_za3rmp$:function(e) {
    var d = this.map, a;
    for (a in d) {
      if (d.hasOwnProperty(a) && d[a] === e) {
        return!0;
      }
    }
    return!1;
  }, get_za3rmp$:function(e) {
    return this.map[e];
  }, put_wn2jw4$:function(e, d) {
    var a = this.map[e];
    this.map[e] = void 0 === d ? null : d;
    void 0 === a && this.$size++;
    return a;
  }, remove_za3rmp$:function(e) {
    var d = this.map[e];
    void 0 !== d && (delete this.map[e], this.$size--);
    return d;
  }, clear:function() {
    this.$size = 0;
    this.map = {};
  }, putAll_za3j1t$:function(e) {
    e = e.map;
    for (var d in e) {
      e.hasOwnProperty(d) && (this.map[d] = e[d], this.$size++);
    }
  }, keySet:function() {
    var e = new Kotlin.PrimitiveHashSet, d = this.map, a;
    for (a in d) {
      d.hasOwnProperty(a) && e.add_za3rmp$(a);
    }
    return e;
  }, values:function() {
    return new h(this);
  }, toJSON:function() {
    return this.map;
  }});
})();
Kotlin.Set = Kotlin.createClassNow(Kotlin.Collection);
var SetIterator = Kotlin.createClassNow(Kotlin.Iterator, function(g) {
  this.set = g;
  this.keys = g.toArray();
  this.index = 0;
}, {next:function() {
  return this.keys[this.index++];
}, hasNext:function() {
  return this.index < this.keys.length;
}, remove:function() {
  this.set.remove_za3rmp$(this.keys[this.index - 1]);
}});
Kotlin.PrimitiveHashSet = Kotlin.createClassNow(Kotlin.AbstractCollection, function() {
  this.$size = 0;
  this.map = {};
}, {contains_s9cetl$:function(g) {
  return!0 === this.map[g];
}, iterator:function() {
  return new SetIterator(this);
}, size:function() {
    return this.$size;
}, add_za3rmp$:function(g) {
  var h = this.map[g];
  this.map[g] = !0;
  if (!0 === h) {
    return!1;
  }
  this.$size++;
  return!0;
}, remove_za3rmp$:function(g) {
  return!0 === this.map[g] ? (delete this.map[g], this.$size--, !0) : !1;
}, clear:function() {
  this.$size = 0;
  this.map = {};
}, toArray:function() {
  return Object.keys(this.map);
}});
(function() {
  function g(h, e) {
    var d = new Kotlin.HashTable(h, e);
    this.addAll_xeylzf$ = Kotlin.AbstractCollection.prototype.addAll_xeylzf$;
    this.removeAll_xeylzf$ = Kotlin.AbstractCollection.prototype.removeAll_xeylzf$;
    this.retainAll_xeylzf$ = Kotlin.AbstractCollection.prototype.retainAll_xeylzf$;
    this.containsAll_xeylzf$ = Kotlin.AbstractCollection.prototype.containsAll_xeylzf$;
    this.add_za3rmp$ = function(a) {
      return!d.put_wn2jw4$(a, !0);
    };
    this.toArray = function() {
      return d._keys();
    };
    this.iterator = function() {
      return new SetIterator(this);
    };
    this.remove_za3rmp$ = function(a) {
      return null != d.remove_za3rmp$(a);
    };
    this.contains_za3rmp$ = function(a) {
      return d.containsKey_za3rmp$(a);
    };
    this.clear = function() {
      d.clear();
    };
    this.size = function() {
      return d.size();
    };
    this.isEmpty = function() {
      return d.isEmpty();
    };
    this.clone = function() {
      var a = new g(h, e);
      a.addAll_xeylzf$(d.keys());
      return a;
    };
    this.equals = function(a) {
      if (null === a || void 0 === a) {
        return!1;
      }
      if (this.size() === a.size()) {
        var b = this.iterator();
        for (a = a.iterator();;) {
          var c = b.hasNext(), d = a.hasNext();
          if (c != d) {
            break;
          }
          if (d) {
            if (c = b.next(), d = a.next(), !Kotlin.equals(c, d)) {
              break;
            }
          } else {
            return!0;
          }
        }
      }
      return!1;
    };
    this.toString = function() {
      for (var a = "[", b = this.iterator(), c = !0;b.hasNext();) {
        c ? c = !1 : a += ", ", a += b.next();
      }
      return a + "]";
    };
    this.intersection = function(a) {
      var b = new g(h, e);
      a = a.values();
      for (var c = a.length, f;c--;) {
        f = a[c], d.containsKey_za3rmp$(f) && b.add_za3rmp$(f);
      }
      return b;
    };
    this.union = function(a) {
      var b = this.clone();
      a = a.values();
      for (var c = a.length, e;c--;) {
        e = a[c], d.containsKey_za3rmp$(e) || b.add_za3rmp$(e);
      }
      return b;
    };
    this.isSubsetOf = function(a) {
      for (var b = d.keys(), c = b.length;c--;) {
        if (!a.contains_za3rmp$(b[c])) {
          return!1;
        }
      }
      return!0;
    };
  }
  Kotlin.HashSet = Kotlin.createClassNow(Kotlin.Set, function() {
    g.call(this);
  });
  Kotlin.ComplexHashSet = Kotlin.HashSet;
})();
(function (Kotlin) {
  'use strict';
  var _ = Kotlin.defineRootPackage(null, /** @lends _ */ {
    pcm: Kotlin.definePackage(null, /** @lends _.pcm */ {
      util: Kotlin.definePackage(function () {
        this.Constants = Kotlin.createObject(null, function () {
          this.UNKNOWN_MUTATION_TYPE_EXCEPTION = 'Unknown mutation type: ';
          this.READ_ONLY_EXCEPTION = 'This model is ReadOnly. Elements are not modifiable.';
          this.LIST_PARAMETER_OF_SET_IS_NULL_EXCEPTION = 'The list in parameter of the setter cannot be null. Use removeAll to empty a collection.';
          this.ELEMENT_HAS_NO_KEY_IN_COLLECTION = 'Cannot set the collection, because at least one element of it has no key!';
          this.EMPTY_KEY = 'Key empty : please set the attribute key before adding the object.';
          this.KMFQL_CONTAINED = 'contained';
          this.STRING_DEFAULTVAL = '';
          this.INT_DEFAULTVAL = 0;
          this.BOOLEAN_DEFAULTVAL = false;
          this.CHAR_DEFAULTVAL = 'a';
          this.SHORT_DEFAULTVAL = 0;
          this.LONG_DEFAULTVAL = 0.0;
          this.DOUBLE_DEFAULTVAL = 0.0;
          this.FLOAT_DEFAULTVAL = 0;
          this.BYTE_DEFAULTVAL = 0;
          this.pcm_Feature = 'pcm.Feature';
          this.pcm_BooleanValue = 'pcm.BooleanValue';
          this.pcm_DateValue = 'pcm.DateValue';
          this.java_lang_Boolean = 'java.lang.Boolean';
          this.pcm_Cell = 'pcm.Cell';
          this.pcm_RealValue = 'pcm.RealValue';
          this.Ref_subvalues = 'subvalues';
          this.Ref_interpretation = 'interpretation';
          this.Att_unit = 'unit';
          this.pcm_StringValue = 'pcm.StringValue';
          this.java_lang_Double = 'java.lang.Double';
          this.pcm_Version = 'pcm.Version';
          this.pcm_FeatureGroup = 'pcm.FeatureGroup';
          this.Ref_condition = 'condition';
          this.Ref_products = 'products';
          this.pcm_PCM = 'pcm.PCM';
          this.pcm_Unit = 'pcm.Unit';
          this.pcm_Multiple = 'pcm.Multiple';
          this.pcm_NotAvailable = 'pcm.NotAvailable';
          this.Ref_values = 'values';
          this.Ref_value = 'value';
          this.Ref_subFeatures = 'subFeatures';
          this.pcm_IntegerValue = 'pcm.IntegerValue';
          this.Att_name = 'name';
          this.pcm_Dimension = 'pcm.Dimension';
          this.Att_content = 'content';
          this.Ref_features = 'features';
          this.java_lang_Integer = 'java.lang.Integer';
          this.java_lang_String = 'java.lang.String';
          this.Att_generated_KMF_ID = 'generated_KMF_ID';
          this.pcm_NotApplicable = 'pcm.NotApplicable';
          this.pcm_Partial = 'pcm.Partial';
          this.Ref_feature = 'feature';
          this.pcm_AbstractFeature = 'pcm.AbstractFeature';
          this.pcm_Product = 'pcm.Product';
          this.pcm_Value = 'pcm.Value';
          this.pcm_Conditional = 'pcm.Conditional';
          this.Att_value = 'value';
        });
      }, /** @lends _.pcm.util */ {
      }),
      container: Kotlin.definePackage(function () {
        this.cleanCacheVisitor = Kotlin.createObject(function () {
          return [_.org.kevoree.modeling.api.util.ModelVisitor];
        }, function $fun() {
          $fun.baseInitializer.call(this);
        }, {
          visit: function (elem, refNameInParent, parent) {
          },
          endVisitElem: function (elem) {
            elem.path_cache = null;
          }
        });
      }, /** @lends _.pcm.container */ {
        RemoveFromContainerCommand: Kotlin.createClass(null, function (target, mutatorType, refName, element) {
          this.target = target;
          this.mutatorType = mutatorType;
          this.refName = refName;
          this.element = element;
        }, /** @lends _.pcm.container.RemoveFromContainerCommand.prototype */ {
          run: function () {
            if (!this.target.isDeleted()) {
              this.target.reflexiveMutator(this.mutatorType, this.refName, this.element, true, true);
            }
          }
        }),
        KMFContainerImpl: Kotlin.createTrait(function () {
          return [_.org.kevoree.modeling.api.util.InboundRefAware, _.org.kevoree.modeling.api.KMFContainer];
        }, /** @lends _.pcm.container.KMFContainerImpl.prototype */ {
          internal_hashcode_c17phv$: {
            get: function () {
              return this.$internal_hashcode_c17phv$;
            },
            set: function (tmp$0) {
              this.$internal_hashcode_c17phv$ = tmp$0;
            }
          },
          hashCode: function () {
            if (this.internal_hashcode_c17phv$ == null) {
              this.internal_hashcode_c17phv$ = Math.floor(Math.random() * 10000000) + (new Date()).getTime();
            }
            var tmp$0;
            return (tmp$0 = this.internal_hashcode_c17phv$) != null ? tmp$0 : Kotlin.throwNPE();
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_pg7vpm$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_pg7vpm$ = tmp$0;
            }
          },
          isDeleted: function () {
            return this.internal_is_deleted;
          },
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_mnh3ua$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_mnh3ua$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_et7q0t$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_et7q0t$ = tmp$0;
            }
          },
          eContainer: function () {
            return this.internal_eContainer;
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_9qezvm$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_9qezvm$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_h5yq83$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_h5yq83$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_n64g9c$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_n64g9c$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_71rbrl$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_71rbrl$ = tmp$0;
            }
          },
          addInboundReference: function (path, refName) {
            if (!this.internal_deleteInProgress) {
              _.kotlin.getOrPut_ynyybx$(this.internal_inboundReferences, path, _.pcm.container.KMFContainerImpl.addInboundReference$f).add_za3rmp$(refName);
            }
          },
          removeInboundReference: function (path, refName) {
            if (!this.internal_deleteInProgress) {
              var refs = this.internal_inboundReferences.get_za3rmp$(path);
              if (refs != null) {
                if (refs.size() > 1) {
                  refs.remove_za3rmp$(refName);
                }
                 else {
                  this.internal_inboundReferences.remove_za3rmp$(path);
                }
              }
            }
          },
          advertiseInboundRefs: function (action, value) {
            {
              var tmp$0 = this.internal_inboundReferences.keySet().iterator();
              while (tmp$0.hasNext()) {
                var inboundElem = tmp$0.next();
                if (!inboundElem.isDeleted()) {
                  var tmp$1;
                  var refList = (tmp$1 = this.internal_inboundReferences.get_za3rmp$(inboundElem)) != null ? tmp$1 : Kotlin.throwNPE();
                  {
                    var tmp$2 = refList.iterator();
                    while (tmp$2.hasNext()) {
                      var ref = tmp$2.next();
                      inboundElem.reflexiveMutator(action, ref, value, false, true);
                    }
                  }
                }
              }
            }
          },
          setRecursiveReadOnly: function () {
            if (Kotlin.equals(this.internal_recursive_readOnlyElem, true)) {
              return;
            }
            this.setInternalRecursiveReadOnly();
            var recVisitor = _.pcm.container.KMFContainerImpl.setRecursiveReadOnly$f();
            this.visit(recVisitor, true, true, true);
            this.setInternalReadOnly();
          },
          setInternalReadOnly: function () {
            this.internal_readOnlyElem = true;
          },
          setInternalRecursiveReadOnly: function () {
            this.internal_recursive_readOnlyElem = true;
          },
          getRefInParent: function () {
            return this.internal_containmentRefName;
          },
          isReadOnly: function () {
            return this.internal_readOnlyElem;
          },
          isRecursiveReadOnly: function () {
            return this.internal_recursive_readOnlyElem;
          },
          setEContainer: function (container, unsetCmd, refNameInParent) {
            if (this.internal_readOnlyElem) {
              return;
            }
            if (Kotlin.equals(this.eContainer(), container)) {
              return;
            }
            this.visit(_.pcm.container.cleanCacheVisitor, true, true, false);
            var tempUnsetCmd = this.internal_unsetCmd;
            this.internal_unsetCmd = null;
            if (tempUnsetCmd != null) {
              tempUnsetCmd.run();
            }
            this.internal_eContainer = container;
            this.internal_unsetCmd = unsetCmd;
            this.internal_containmentRefName = refNameInParent;
            this.path_cache = null;
          },
          select: function (query) {
            if (Kotlin.equals(this.path(), '/') && query.startsWith('/')) {
              return _.org.kevoree.modeling.api.util.Selector.select(this, query.substring(1));
            }
             else {
              return _.org.kevoree.modeling.api.util.Selector.select(this, query);
            }
          },
          addModelElementListener: function (lst) {
            throw new Error('Not activated, please add events option in KMF generation plugin');
          },
          removeModelElementListener: function (lst) {
            throw new Error('Not activated, please add events option in KMF generation plugin');
          },
          removeAllModelElementListeners: function () {
            throw new Error('Not activated, please add events option in KMF generation plugin');
          },
          addModelTreeListener: function (lst) {
            throw new Error('Not activated, please add events option in KMF generation plugin');
          },
          removeModelTreeListener: function (lst) {
            throw new Error('Not activated, please add events option in KMF generation plugin');
          },
          removeAllModelTreeListeners: function () {
            throw new Error('Not activated, please add events option in KMF generation plugin');
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
          },
          visitAttributes: function (visitor) {
          },
          internal_visit: function (visitor, internalElem, recursive, containedReference, nonContainedReference, refName) {
            if (internalElem != null) {
              if (nonContainedReference && recursive) {
                var elemPath = internalElem.path();
                var tmp$0, tmp$1;
                if (visitor.alreadyVisited != null && ((tmp$0 = visitor.alreadyVisited) != null ? tmp$0 : Kotlin.throwNPE()).containsKey_za3rmp$(elemPath)) {
                  return;
                }
                if (visitor.alreadyVisited == null) {
                  visitor.alreadyVisited = new Kotlin.PrimitiveHashMap();
                }
                ((tmp$1 = visitor.alreadyVisited) != null ? tmp$1 : Kotlin.throwNPE()).put_wn2jw4$(elemPath, internalElem);
              }
              visitor.visit(internalElem, refName, this);
              if (!visitor.visitStopped) {
                if (recursive && (visitor.visitChildren || visitor.visitReferences)) {
                  var visitSubReferences = nonContainedReference && visitor.visitReferences;
                  var visitSubChilds = containedReference && visitor.visitChildren;
                  internalElem.visit(visitor, recursive, visitSubChilds, visitSubReferences);
                }
                visitor.visitChildren = true;
                visitor.visitReferences = true;
              }
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_2tj5yw$;
            },
            set: function (tmp$0) {
              this.$path_cache_2tj5yw$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_ayc3v6$;
            },
            set: function (tmp$0) {
              this.$key_cache_ayc3v6$ = tmp$0;
            }
          },
          isRoot: function () {
            return this.is_root;
          },
          is_root: {
            get: function () {
              return this.$is_root_zar0op$;
            },
            set: function (tmp$0) {
              this.$is_root_zar0op$ = tmp$0;
            }
          },
          path: function () {
            if (this.path_cache != null) {
              var tmp$0;
              return (tmp$0 = this.path_cache) != null ? tmp$0 : Kotlin.throwNPE();
            }
            var container = this.eContainer();
            if (container != null) {
              var parentPath = container.path();
              if (Kotlin.equals(parentPath, '')) {
                var tmp$1;
                this.path_cache = ((tmp$1 = this.internal_containmentRefName) != null ? tmp$1 : Kotlin.throwNPE()) + '[' + this.internalGetKey() + ']';
              }
               else if (Kotlin.equals(parentPath, '/')) {
                var tmp$2;
                this.path_cache = parentPath + ((tmp$2 = this.internal_containmentRefName) != null ? tmp$2 : Kotlin.throwNPE()) + '[' + this.internalGetKey() + ']';
              }
               else {
                var tmp$3;
                this.path_cache = parentPath + '/' + ((tmp$3 = this.internal_containmentRefName) != null ? tmp$3 : Kotlin.throwNPE()) + '[' + this.internalGetKey() + ']';
              }
            }
             else {
              if (this.is_root) {
                this.path_cache = '/';
              }
               else {
                this.path_cache = '';
              }
            }
            var tmp$4;
            return (tmp$4 = this.path_cache) != null ? tmp$4 : Kotlin.throwNPE();
          },
          modelEquals: function (similarObj) {
            if (similarObj == null) {
              return false;
            }
            if (Kotlin.equals(this, similarObj)) {
              return true;
            }
            if (!Kotlin.equals(similarObj.metaClassName(), this.metaClassName())) {
              return false;
            }
            var values = new Kotlin.PrimitiveHashMap();
            var attVisitor = _.pcm.container.KMFContainerImpl.modelEquals$f(values);
            this.visitAttributes(attVisitor);
            similarObj.visitAttributes(attVisitor);
            if (!values.isEmpty()) {
              return false;
            }
            var payload = '';
            var refVisitor = _.pcm.container.KMFContainerImpl.modelEquals$f_0(values, payload);
            this.visit(refVisitor, false, false, true);
            similarObj.visit(refVisitor, false, false, true);
            if (!values.isEmpty()) {
              return false;
            }
            return true;
          },
          deepModelEquals: function (similarObj) {
            if (!this.modelEquals(similarObj)) {
              return false;
            }
            var similarRoot = similarObj != null ? similarObj : Kotlin.throwNPE();
            while (similarRoot.eContainer() != null) {
              var tmp$0;
              similarRoot = (tmp$0 = similarRoot.eContainer()) != null ? tmp$0 : Kotlin.throwNPE();
            }
            var resultTest = {v: true};
            var finalRoot = similarRoot;
            var objVisitor = _.pcm.container.KMFContainerImpl.deepModelEquals$f(finalRoot, resultTest);
            this.visit(objVisitor, true, true, false);
            return resultTest.v;
          },
          findByPath: function (query) {
            if (Kotlin.equals(query, this.path())) {
              return this;
            }
            if (Kotlin.equals(this.path(), '/') && query.startsWith('/')) {
              return this.findByPath(query.substring(1));
            }
            var firstSepIndex = _.js.indexOf_960177$(query, '[');
            if (firstSepIndex === -1) {
              if (query.length === 0) {
                return this;
              }
               else {
                return null;
              }
            }
            var queryID = '';
            var extraReadChar = 2;
            var relationName = query.substring(0, _.js.indexOf_960177$(query, '['));
            if (_.js.indexOf_960177$(query, '{') === firstSepIndex + 1) {
              queryID = query.substring(_.js.indexOf_960177$(query, '{') + 1, _.js.indexOf_960177$(query, '}'));
              extraReadChar = extraReadChar + 2;
            }
             else {
              var indexFirstClose = _.js.indexOf_960177$(query, ']');
              while (indexFirstClose + 1 < query.length && query.charAt(indexFirstClose + 1) !== '/') {
                indexFirstClose = _.js.indexOf_orzsrp$(query, ']', indexFirstClose + 1);
                if (indexFirstClose === -1) {
                  return null;
                }
              }
              queryID = query.substring(_.js.indexOf_960177$(query, '[') + 1, indexFirstClose);
            }
            var subquery = query.substring(relationName.length + queryID.length + extraReadChar, query.length);
            if (_.js.indexOf_960177$(subquery, '/') !== -1) {
              subquery = subquery.substring(_.js.indexOf_960177$(subquery, '/') + 1, subquery.length);
            }
            var objFound = this.findByID(relationName, queryID);
            if (!Kotlin.equals(subquery, '') && objFound != null) {
              return objFound.findByPath(subquery);
            }
             else {
              return objFound;
            }
          },
          createTraces: function (similarObj, isInter, isMerge, onlyReferences, onlyAttributes) {
            var traces = new Kotlin.ArrayList();
            var values = new Kotlin.PrimitiveHashMap();
            if (onlyAttributes) {
              var attVisitorFill = _.pcm.container.KMFContainerImpl.createTraces$f(values);
              this.visitAttributes(attVisitorFill);
              var attVisitor = _.pcm.container.KMFContainerImpl.createTraces$f_0(values, isInter, traces, this);
              if (similarObj != null) {
                similarObj.visitAttributes(attVisitor);
              }
              if (!isInter && !isMerge && _.kotlin.get_size(values) !== 0) {
                {
                  var tmp$0 = values.keySet().iterator();
                  while (tmp$0.hasNext()) {
                    var hashLoopRes = tmp$0.next();
                    traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelSetTrace(this.path(), hashLoopRes, null, null, null));
                  }
                }
              }
            }
            if (onlyReferences) {
              var payload = '';
              var refVisitorFill = _.pcm.container.KMFContainerImpl.createTraces$f_1(values, payload);
              this.visit(refVisitorFill, false, false, true);
              var refVisitor = _.pcm.container.KMFContainerImpl.createTraces$f_2(values, isInter, traces, this);
              if (similarObj != null) {
                similarObj.visit(refVisitor, false, false, true);
              }
              if (!isInter && !isMerge && _.kotlin.get_size(values) !== 0) {
                {
                  var tmp$1 = values.keySet().iterator();
                  while (tmp$1.hasNext()) {
                    var hashLoopRes_0 = tmp$1.next();
                    var splittedVal = Kotlin.splitString(hashLoopRes_0, '_');
                    traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelRemoveTrace(this.path(), splittedVal[0], splittedVal[1]));
                  }
                }
              }
            }
            return traces;
          },
          toTraces: function (attributes, references) {
            var traces = new Kotlin.ArrayList();
            if (attributes) {
              var attVisitorFill = _.pcm.container.KMFContainerImpl.toTraces$f(traces, this);
              this.visitAttributes(attVisitorFill);
            }
            if (references) {
              var refVisitorFill = _.pcm.container.KMFContainerImpl.toTraces$f_0(traces, this);
              this.visit(refVisitorFill, false, true, true);
            }
            return traces;
          },
          visitNotContained: function (visitor) {
            this.visit(visitor, false, false, true);
          },
          visitContained: function (visitor) {
            this.visit(visitor, false, true, false);
          },
          visitReferences: function (visitor) {
            this.visit(visitor, false, true, true);
          },
          deepVisitNotContained: function (visitor) {
            this.visit(visitor, true, false, true);
          },
          deepVisitContained: function (visitor) {
            this.visit(visitor, true, true, false);
          },
          deepVisitReferences: function (visitor) {
            this.visit(visitor, true, true, true);
          }
        }, /** @lends _.pcm.container.KMFContainerImpl */ {
          addInboundReference$f: function () {
            return new Kotlin.PrimitiveHashSet();
          },
          setRecursiveReadOnly$f: function () {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelVisitor];
            }, function $fun() {
              $fun.baseInitializer.call(this);
            }, {
              visit: function (elem, refNameInParent, parent) {
                if (elem.isRecursiveReadOnly()) {
                  this.noChildrenVisit();
                }
                 else {
                  elem.setInternalRecursiveReadOnly();
                  elem.setInternalReadOnly();
                }
              }
            });
          },
          modelEquals$f: function (values) {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelAttributeVisitor];
            }, null, {
              visit: function (value, name, parent) {
                if (values.containsKey_za3rmp$(name)) {
                  if (Kotlin.equals(values.get_za3rmp$(name), value != null ? value.toString() : null)) {
                    values.remove_za3rmp$(name);
                  }
                }
                 else {
                  values.put_wn2jw4$(name, value != null ? value.toString() : null);
                }
              }
            });
          },
          modelEquals$f_0: function (values, payload) {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelVisitor];
            }, function $fun() {
              $fun.baseInitializer.call(this);
            }, {
              visit: function (elem, refNameInParent, parent) {
                var concatedKey = refNameInParent + '_' + elem.path();
                if (values.containsKey_za3rmp$(concatedKey)) {
                  values.remove_za3rmp$(concatedKey);
                }
                 else {
                  values.put_wn2jw4$(concatedKey, payload);
                }
              }
            });
          },
          deepModelEquals$f: function (finalRoot, resultTest) {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelVisitor];
            }, function $fun() {
              $fun.baseInitializer.call(this);
            }, {
              visit: function (elem, refNameInParent, parent) {
                var similarSubObj = finalRoot.findByPath(elem.path());
                if (!elem.modelEquals(similarSubObj)) {
                  resultTest.v = false;
                  this.stopVisit();
                }
              }
            });
          },
          createTraces$f: function (values) {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelAttributeVisitor];
            }, null, {
              visit: function (value, name, parent) {
                values.put_wn2jw4$(name, _.org.kevoree.modeling.api.util.AttConverter.convFlatAtt(value));
              }
            });
          },
          createTraces$f_0: function (values, isInter, traces, this$KMFContainerImpl) {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelAttributeVisitor];
            }, null, {
              visit: function (value, name, parent) {
                var attVal2 = _.org.kevoree.modeling.api.util.AttConverter.convFlatAtt(value);
                if (Kotlin.equals(values.get_za3rmp$(name), attVal2)) {
                  if (isInter) {
                    traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelSetTrace(this$KMFContainerImpl.path(), name, null, attVal2, null));
                  }
                }
                 else {
                  if (!isInter) {
                    traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelSetTrace(this$KMFContainerImpl.path(), name, null, attVal2, null));
                  }
                }
                values.remove_za3rmp$(name);
              }
            });
          },
          createTraces$f_1: function (values, payload) {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelVisitor];
            }, function $fun() {
              $fun.baseInitializer.call(this);
            }, {
              visit: function (elem, refNameInParent, parent) {
                var concatedKey = refNameInParent + '_' + elem.path();
                values.put_wn2jw4$(concatedKey, payload);
              }
            });
          },
          createTraces$f_2: function (values, isInter, traces, this$KMFContainerImpl) {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelVisitor];
            }, function $fun() {
              $fun.baseInitializer.call(this);
            }, {
              visit: function (elem, refNameInParent, parent) {
                var concatedKey = refNameInParent + '_' + elem.path();
                if (values.get_za3rmp$(concatedKey) != null) {
                  if (isInter) {
                    traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace(this$KMFContainerImpl.path(), refNameInParent, elem.path(), null));
                  }
                }
                 else {
                  if (!isInter) {
                    traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace(this$KMFContainerImpl.path(), refNameInParent, elem.path(), null));
                  }
                }
                values.remove_za3rmp$(concatedKey);
              }
            });
          },
          toTraces$f: function (traces, this$KMFContainerImpl) {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelAttributeVisitor];
            }, null, {
              visit: function (value, name, parent) {
                traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelSetTrace(this$KMFContainerImpl.path(), name, null, _.org.kevoree.modeling.api.util.AttConverter.convFlatAtt(value), null));
              }
            });
          },
          toTraces$f_0: function (traces, this$KMFContainerImpl) {
            return Kotlin.createObject(function () {
              return [_.org.kevoree.modeling.api.util.ModelVisitor];
            }, function $fun() {
              $fun.baseInitializer.call(this);
            }, {
              visit: function (elem, refNameInParent, parent) {
                traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace(this$KMFContainerImpl.path(), refNameInParent, elem.path(), null));
              }
            });
          }
        })
      }),
      PCM: Kotlin.createTrait(function () {
        return [_.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.PCM.prototype */ {
        name: {
          get: function () {
            return this.$name_unk2qb$;
          },
          set: function (tmp$0) {
            this.$name_unk2qb$ = tmp$0;
          }
        },
        generated_KMF_ID: {
          get: function () {
            return this.$generated_KMF_ID_urnifi$;
          },
          set: function (tmp$0) {
            this.$generated_KMF_ID_urnifi$ = tmp$0;
          }
        },
        products: {
          get: function () {
            return this.$products_jth36c$;
          },
          set: function (tmp$0) {
            this.$products_jth36c$ = tmp$0;
          }
        },
        features: {
          get: function () {
            return this.$features_80wu4b$;
          },
          set: function (tmp$0) {
            this.$features_80wu4b$ = tmp$0;
          }
        }
      }),
      Product: Kotlin.createTrait(function () {
        return [_.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.Product.prototype */ {
        name: {
          get: function () {
            return this.$name_jd6bz6$;
          },
          set: function (tmp$0) {
            this.$name_jd6bz6$ = tmp$0;
          }
        },
        generated_KMF_ID: {
          get: function () {
            return this.$generated_KMF_ID_cztrbt$;
          },
          set: function (tmp$0) {
            this.$generated_KMF_ID_cztrbt$ = tmp$0;
          }
        },
        values: {
          get: function () {
            return this.$values_35teb9$;
          },
          set: function (tmp$0) {
            this.$values_35teb9$ = tmp$0;
          }
        }
      }),
      AbstractFeature: Kotlin.createTrait(function () {
        return [_.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.AbstractFeature.prototype */ {
        name: {
          get: function () {
            return this.$name_7wovax$;
          },
          set: function (tmp$0) {
            this.$name_7wovax$ = tmp$0;
          }
        },
        generated_KMF_ID: {
          get: function () {
            return this.$generated_KMF_ID_5y6wy4$;
          },
          set: function (tmp$0) {
            this.$generated_KMF_ID_5y6wy4$ = tmp$0;
          }
        }
      }),
      Cell: Kotlin.createTrait(function () {
        return [_.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.Cell.prototype */ {
        content: {
          get: function () {
            return this.$content_k57d5r$;
          },
          set: function (tmp$0) {
            this.$content_k57d5r$ = tmp$0;
          }
        },
        generated_KMF_ID: {
          get: function () {
            return this.$generated_KMF_ID_2pcwpq$;
          },
          set: function (tmp$0) {
            this.$generated_KMF_ID_2pcwpq$ = tmp$0;
          }
        },
        feature: {
          get: function () {
            return this.$feature_iye9su$;
          },
          set: function (tmp$0) {
            this.$feature_iye9su$ = tmp$0;
          }
        },
        interpretation: {
          get: function () {
            return this.$interpretation_88197k$;
          },
          set: function (tmp$0) {
            this.$interpretation_88197k$ = tmp$0;
          }
        }
      }),
      Feature: Kotlin.createTrait(function () {
        return [_.pcm.AbstractFeature, _.org.kevoree.modeling.api.KMFContainer];
      }),
      FeatureGroup: Kotlin.createTrait(function () {
        return [_.pcm.AbstractFeature, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.FeatureGroup.prototype */ {
        subFeatures: {
          get: function () {
            return this.$subFeatures_6vj3lg$;
          },
          set: function (tmp$0) {
            this.$subFeatures_6vj3lg$ = tmp$0;
          }
        }
      }),
      Value: Kotlin.createTrait(function () {
        return [_.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.Value.prototype */ {
        generated_KMF_ID: {
          get: function () {
            return this.$generated_KMF_ID_spkxg7$;
          },
          set: function (tmp$0) {
            this.$generated_KMF_ID_spkxg7$ = tmp$0;
          }
        }
      }),
      IntegerValue: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.IntegerValue.prototype */ {
        value: {
          get: function () {
            return this.$value_7il22e$;
          },
          set: function (tmp$0) {
            this.$value_7il22e$ = tmp$0;
          }
        }
      }),
      StringValue: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.StringValue.prototype */ {
        value: {
          get: function () {
            return this.$value_rk557z$;
          },
          set: function (tmp$0) {
            this.$value_rk557z$ = tmp$0;
          }
        }
      }),
      RealValue: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.RealValue.prototype */ {
        value: {
          get: function () {
            return this.$value_dyjf2a$;
          },
          set: function (tmp$0) {
            this.$value_dyjf2a$ = tmp$0;
          }
        }
      }),
      BooleanValue: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.BooleanValue.prototype */ {
        value: {
          get: function () {
            return this.$value_jh0h0g$;
          },
          set: function (tmp$0) {
            this.$value_jh0h0g$ = tmp$0;
          }
        }
      }),
      Multiple: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.Multiple.prototype */ {
        subvalues: {
          get: function () {
            return this.$subvalues_dbh6m0$;
          },
          set: function (tmp$0) {
            this.$subvalues_dbh6m0$ = tmp$0;
          }
        }
      }),
      NotAvailable: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }),
      Conditional: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.Conditional.prototype */ {
        value: {
          get: function () {
            return this.$value_h9e2az$;
          },
          set: function (tmp$0) {
            this.$value_h9e2az$ = tmp$0;
          }
        },
        condition: {
          get: function () {
            return this.$condition_4o6u81$;
          },
          set: function (tmp$0) {
            this.$condition_4o6u81$ = tmp$0;
          }
        }
      }),
      Partial: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.Partial.prototype */ {
        value: {
          get: function () {
            return this.$value_psaeeo$;
          },
          set: function (tmp$0) {
            this.$value_psaeeo$ = tmp$0;
          }
        }
      }),
      DateValue: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.DateValue.prototype */ {
        value: {
          get: function () {
            return this.$value_hef84y$;
          },
          set: function (tmp$0) {
            this.$value_hef84y$ = tmp$0;
          }
        }
      }),
      Version: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }),
      Dimension: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }),
      NotApplicable: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }),
      Unit: Kotlin.createTrait(function () {
        return [_.pcm.Value, _.org.kevoree.modeling.api.KMFContainer];
      }, /** @lends _.pcm.Unit.prototype */ {
        unit: {
          get: function () {
            return this.$unit_2pxu9y$;
          },
          set: function (tmp$0) {
            this.$unit_2pxu9y$ = tmp$0;
          }
        },
        value: {
          get: function () {
            return this.$value_dapghx$;
          },
          set: function (tmp$0) {
            this.$value_dapghx$ = tmp$0;
          }
        }
      }),
      impl: Kotlin.definePackage(null, /** @lends _.pcm.impl */ {
        MultipleImpl: Kotlin.createClass(function () {
          return [_.pcm.Multiple, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_bqi3vq$ = null;
          this.$internal_containmentRefName_l4adm2$ = null;
          this.$internal_unsetCmd_9fyr67$ = null;
          this.$internal_readOnlyElem_58fgoh$ = false;
          this.$internal_recursive_readOnlyElem_mpx0sc$ = false;
          this.$internal_inboundReferences_xbrw9p$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_s0nnsr$ = false;
          this.$internal_is_deleted_ej8vr2$ = false;
          this.$is_root_wsgmkz$ = false;
          this.$path_cache_jkklik$ = null;
          this.$key_cache_p8ppni$ = null;
          this.$generated_KMF_ID_tlznwq$ = '' + Math.random() + (new Date()).getTime();
          this._subvalues = new _.java.util.concurrent.ConcurrentHashMap();
        }, /** @lends _.pcm.impl.MultipleImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_bqi3vq$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_bqi3vq$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_l4adm2$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_l4adm2$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_9fyr67$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_9fyr67$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_58fgoh$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_58fgoh$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_mpx0sc$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_mpx0sc$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_xbrw9p$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_xbrw9p$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_s0nnsr$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_s0nnsr$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_ej8vr2$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_ej8vr2$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_wsgmkz$;
            },
            set: function (tmp$0) {
              this.$is_root_wsgmkz$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_jkklik$;
            },
            set: function (tmp$0) {
              this.$path_cache_jkklik$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_p8ppni$;
            },
            set: function (tmp$0) {
              this.$key_cache_p8ppni$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            {
              var tmp$0 = this.subvalues.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                el.delete();
              }
            }
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$1;
              ((tmp$1 = this.internal_unsetCmd) != null ? tmp$1 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_tlznwq$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_tlznwq$;
                this.$generated_KMF_ID_tlznwq$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          subvalues: {
            get: function () {
              return _.kotlin.toList_h3panj$(this._subvalues.values());
            },
            set: function (subvaluesP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (subvaluesP == null) {
                throw new Kotlin.IllegalArgumentException(_.pcm.util.Constants.LIST_PARAMETER_OF_SET_IS_NULL_EXCEPTION);
              }
              this.internal_subvalues(subvaluesP, true, true);
            }
          },
          internal_subvalues: function (subvaluesP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this._subvalues.values(), subvaluesP)) {
              var kmf_previousVal = this._subvalues;
              this._subvalues.clear();
              {
                var tmp$0 = subvaluesP.iterator();
                while (tmp$0.hasNext()) {
                  var el = tmp$0.next();
                  var elKey = el.internalGetKey();
                  if (elKey == null) {
                    throw new Error(_.pcm.util.Constants.ELEMENT_HAS_NO_KEY_IN_COLLECTION);
                  }
                  this._subvalues.put_wn2jw4$(elKey != null ? elKey : Kotlin.throwNPE(), el);
                  el.addInboundReference(this, _.pcm.util.Constants.Ref_subvalues);
                  el.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_subvalues, el), _.pcm.util.Constants.Ref_subvalues);
                }
              }
            }
          },
          doAddSubvalues: function (subvaluesP) {
            var _key_ = subvaluesP.internalGetKey();
            if (_key_ == null || Kotlin.equals(_key_, '')) {
              throw new Error(_.pcm.util.Constants.EMPTY_KEY);
            }
            if (!this._subvalues.containsKey_za3rmp$(_key_)) {
              this._subvalues.put_wn2jw4$(_key_, subvaluesP);
              subvaluesP.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_subvalues, subvaluesP), _.pcm.util.Constants.Ref_subvalues);
              subvaluesP.addInboundReference(this, _.pcm.util.Constants.Ref_subvalues);
            }
          },
          addSubvalues: function (subvaluesP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            this.doAddSubvalues(subvaluesP);
            return this;
          },
          addAllSubvalues: function (subvaluesP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            {
              var tmp$0 = subvaluesP.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                this.doAddSubvalues(el);
              }
            }
            return this;
          },
          removeSubvalues: function (subvaluesP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            if (_.kotlin.get_size(this._subvalues) === 2 && this._subvalues.containsKey_za3rmp$(subvaluesP.internalGetKey())) {
              throw new Kotlin.UnsupportedOperationException('The list of subvaluesP must contain at least 2 element. Can not remove sizeof(subvaluesP)=' + _.kotlin.get_size(this._subvalues));
            }
             else {
              this._subvalues.remove_za3rmp$(subvaluesP.internalGetKey());
              subvaluesP.removeInboundReference(this, _.pcm.util.Constants.Ref_subvalues);
              subvaluesP.setEContainer(null, null, null);
            }
            return this;
          },
          removeAllSubvalues: function () {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            var temp_els = this.subvalues;
            {
              var tmp$0 = temp_els.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                el.removeInboundReference(this, _.pcm.util.Constants.Ref_subvalues);
                el.setEContainer(null, null, null);
              }
            }
            this._subvalues.clear();
            return this;
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Ref_subvalues) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.addSubvalues(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL) {
                    this.addAllSubvalues(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.removeSubvalues(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL) {
                    this.removeAllSubvalues();
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                    if (this._subvalues.size() !== 0 && this._subvalues.containsKey_za3rmp$(value)) {
                      var obj = this._subvalues.get_za3rmp$(value);
                      var objNewKey = (obj != null ? obj : Kotlin.throwNPE()).internalGetKey();
                      if (objNewKey == null) {
                        throw new Error('Key newed to null ' + obj);
                      }
                      this._subvalues.remove_za3rmp$(value);
                      this._subvalues.put_wn2jw4$(objNewKey, obj);
                    }
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findSubvaluesByID: function (key) {
            return this._subvalues.get_za3rmp$(key);
          },
          findByID: function (relationName, idP) {
            {
              if (relationName === _.pcm.util.Constants.Ref_subvalues) {
                return this.findSubvaluesByID(idP);
              }
               else {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            if (containedReference) {
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_subvalues, _.pcm.util.Constants.pcm_Value)) {
                {
                  var tmp$0 = this._subvalues.keySet().iterator();
                  while (tmp$0.hasNext()) {
                    var KMFLoopEntryKey = tmp$0.next();
                    this.internal_visit(visitor, this._subvalues.get_za3rmp$(KMFLoopEntryKey), recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_subvalues);
                  }
                }
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_subvalues);
            }
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Multiple;
          }
        }),
        FeatureImpl: Kotlin.createClass(function () {
          return [_.pcm.Feature, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_dhdrqs$ = null;
          this.$internal_containmentRefName_b2gs7w$ = null;
          this.$internal_unsetCmd_6xfh1d$ = null;
          this.$internal_readOnlyElem_kz1upt$ = false;
          this.$internal_recursive_readOnlyElem_ye6gmm$ = false;
          this.$internal_inboundReferences_65lh3j$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_a22qqb$ = false;
          this.$internal_is_deleted_ga4jm4$ = false;
          this.$is_root_k7ha6d$ = false;
          this.$path_cache_5cp1vq$ = null;
          this.$key_cache_8ecvf4$ = null;
          this.$name_rffgkj$ = _.pcm.util.Constants.STRING_DEFAULTVAL;
          this.$generated_KMF_ID_9f7trc$ = '' + Math.random() + (new Date()).getTime();
        }, /** @lends _.pcm.impl.FeatureImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_dhdrqs$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_dhdrqs$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_b2gs7w$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_b2gs7w$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_6xfh1d$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_6xfh1d$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_kz1upt$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_kz1upt$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_ye6gmm$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_ye6gmm$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_65lh3j$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_65lh3j$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_a22qqb$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_a22qqb$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_ga4jm4$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_ga4jm4$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_k7ha6d$;
            },
            set: function (tmp$0) {
              this.$is_root_k7ha6d$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_5cp1vq$;
            },
            set: function (tmp$0) {
              this.$path_cache_5cp1vq$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_8ecvf4$;
            },
            set: function (tmp$0) {
              this.$key_cache_8ecvf4$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withName: function (p) {
            this.name = p;
            return this;
          },
          name: {
            get: function () {
              return this.$name_rffgkj$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.name)) {
                var kmf_previousVal = this.$name_rffgkj$;
                this.$name_rffgkj$ = iP;
              }
            }
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_9f7trc$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_9f7trc$;
                this.$generated_KMF_ID_9f7trc$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_name) {
                this.name = value;
              }
               else if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.name, _.pcm.util.Constants.Att_name, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Feature;
          }
        }),
        PCMImpl: Kotlin.createClass(function () {
          return [_.pcm.PCM, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_xoff3k$ = null;
          this.$internal_containmentRefName_1v3tc0$ = null;
          this.$internal_unsetCmd_wx61np$ = null;
          this.$internal_readOnlyElem_pdei79$ = false;
          this.$internal_recursive_readOnlyElem_yrv91u$ = false;
          this.$internal_inboundReferences_n7q0gd$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_kkemzz$ = false;
          this.$internal_is_deleted_uvon88$ = false;
          this.$is_root_jtuno7$ = false;
          this.$path_cache_3msixi$ = null;
          this.$key_cache_vlo1dg$ = null;
          this.$name_ehwpaf$ = _.pcm.util.Constants.STRING_DEFAULTVAL;
          this.$generated_KMF_ID_ebuc6s$ = '' + Math.random() + (new Date()).getTime();
          this._features = new _.java.util.concurrent.ConcurrentHashMap();
          this._products = new _.java.util.concurrent.ConcurrentHashMap();
        }, /** @lends _.pcm.impl.PCMImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_xoff3k$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_xoff3k$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_1v3tc0$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_1v3tc0$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_wx61np$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_wx61np$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_pdei79$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_pdei79$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_yrv91u$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_yrv91u$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_n7q0gd$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_n7q0gd$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_kkemzz$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_kkemzz$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_uvon88$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_uvon88$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_jtuno7$;
            },
            set: function (tmp$0) {
              this.$is_root_jtuno7$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_3msixi$;
            },
            set: function (tmp$0) {
              this.$path_cache_3msixi$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_vlo1dg$;
            },
            set: function (tmp$0) {
              this.$key_cache_vlo1dg$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            {
              var tmp$0 = this.products.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                el.delete();
              }
            }
            {
              var tmp$1 = this.features.iterator();
              while (tmp$1.hasNext()) {
                var el_0 = tmp$1.next();
                el_0.delete();
              }
            }
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$2;
              ((tmp$2 = this.internal_unsetCmd) != null ? tmp$2 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withName: function (p) {
            this.name = p;
            return this;
          },
          name: {
            get: function () {
              return this.$name_ehwpaf$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.name)) {
                var kmf_previousVal = this.$name_ehwpaf$;
                this.$name_ehwpaf$ = iP;
              }
            }
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_ebuc6s$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_ebuc6s$;
                this.$generated_KMF_ID_ebuc6s$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          features: {
            get: function () {
              return _.kotlin.toList_h3panj$(this._features.values());
            },
            set: function (featuresP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (featuresP == null) {
                throw new Kotlin.IllegalArgumentException(_.pcm.util.Constants.LIST_PARAMETER_OF_SET_IS_NULL_EXCEPTION);
              }
              this.internal_features(featuresP, true, true);
            }
          },
          internal_features: function (featuresP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this._features.values(), featuresP)) {
              var kmf_previousVal = this._features;
              this._features.clear();
              {
                var tmp$0 = featuresP.iterator();
                while (tmp$0.hasNext()) {
                  var el = tmp$0.next();
                  var elKey = el.internalGetKey();
                  if (elKey == null) {
                    throw new Error(_.pcm.util.Constants.ELEMENT_HAS_NO_KEY_IN_COLLECTION);
                  }
                  this._features.put_wn2jw4$(elKey != null ? elKey : Kotlin.throwNPE(), el);
                  el.addInboundReference(this, _.pcm.util.Constants.Ref_features);
                  el.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_features, el), _.pcm.util.Constants.Ref_features);
                }
              }
            }
          },
          doAddFeatures: function (featuresP) {
            var _key_ = featuresP.internalGetKey();
            if (_key_ == null || Kotlin.equals(_key_, '')) {
              throw new Error(_.pcm.util.Constants.EMPTY_KEY);
            }
            if (!this._features.containsKey_za3rmp$(_key_)) {
              this._features.put_wn2jw4$(_key_, featuresP);
              featuresP.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_features, featuresP), _.pcm.util.Constants.Ref_features);
              featuresP.addInboundReference(this, _.pcm.util.Constants.Ref_features);
            }
          },
          addFeatures: function (featuresP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            this.doAddFeatures(featuresP);
            return this;
          },
          addAllFeatures: function (featuresP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            {
              var tmp$0 = featuresP.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                this.doAddFeatures(el);
              }
            }
            return this;
          },
          removeFeatures: function (featuresP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            if (this._features.size() !== 0 && this._features.containsKey_za3rmp$(featuresP.internalGetKey())) {
              this._features.remove_za3rmp$(featuresP.internalGetKey());
              featuresP.removeInboundReference(this, _.pcm.util.Constants.Ref_features);
              featuresP.setEContainer(null, null, null);
            }
            return this;
          },
          removeAllFeatures: function () {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            var temp_els = this.features;
            {
              var tmp$0 = temp_els.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                el.removeInboundReference(this, _.pcm.util.Constants.Ref_features);
                el.setEContainer(null, null, null);
              }
            }
            this._features.clear();
            return this;
          },
          products: {
            get: function () {
              return _.kotlin.toList_h3panj$(this._products.values());
            },
            set: function (productsP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (productsP == null) {
                throw new Kotlin.IllegalArgumentException(_.pcm.util.Constants.LIST_PARAMETER_OF_SET_IS_NULL_EXCEPTION);
              }
              this.internal_products(productsP, true, true);
            }
          },
          internal_products: function (productsP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this._products.values(), productsP)) {
              var kmf_previousVal = this._products;
              this._products.clear();
              {
                var tmp$0 = productsP.iterator();
                while (tmp$0.hasNext()) {
                  var el = tmp$0.next();
                  var elKey = el.internalGetKey();
                  if (elKey == null) {
                    throw new Error(_.pcm.util.Constants.ELEMENT_HAS_NO_KEY_IN_COLLECTION);
                  }
                  this._products.put_wn2jw4$(elKey != null ? elKey : Kotlin.throwNPE(), el);
                  el.addInboundReference(this, _.pcm.util.Constants.Ref_products);
                  el.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_products, el), _.pcm.util.Constants.Ref_products);
                }
              }
            }
          },
          doAddProducts: function (productsP) {
            var _key_ = productsP.internalGetKey();
            if (_key_ == null || Kotlin.equals(_key_, '')) {
              throw new Error(_.pcm.util.Constants.EMPTY_KEY);
            }
            if (!this._products.containsKey_za3rmp$(_key_)) {
              this._products.put_wn2jw4$(_key_, productsP);
              productsP.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_products, productsP), _.pcm.util.Constants.Ref_products);
              productsP.addInboundReference(this, _.pcm.util.Constants.Ref_products);
            }
          },
          addProducts: function (productsP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            this.doAddProducts(productsP);
            return this;
          },
          addAllProducts: function (productsP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            {
              var tmp$0 = productsP.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                this.doAddProducts(el);
              }
            }
            return this;
          },
          removeProducts: function (productsP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            if (this._products.size() !== 0 && this._products.containsKey_za3rmp$(productsP.internalGetKey())) {
              this._products.remove_za3rmp$(productsP.internalGetKey());
              productsP.removeInboundReference(this, _.pcm.util.Constants.Ref_products);
              productsP.setEContainer(null, null, null);
            }
            return this;
          },
          removeAllProducts: function () {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            var temp_els = this.products;
            {
              var tmp$0 = temp_els.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                el.removeInboundReference(this, _.pcm.util.Constants.Ref_products);
                el.setEContainer(null, null, null);
              }
            }
            this._products.clear();
            return this;
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_name) {
                this.name = value;
              }
               else if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Ref_products) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.addProducts(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL) {
                    this.addAllProducts(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.removeProducts(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL) {
                    this.removeAllProducts();
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                    if (this._products.size() !== 0 && this._products.containsKey_za3rmp$(value)) {
                      var obj = this._products.get_za3rmp$(value);
                      var objNewKey = (obj != null ? obj : Kotlin.throwNPE()).internalGetKey();
                      if (objNewKey == null) {
                        throw new Error('Key newed to null ' + obj);
                      }
                      this._products.remove_za3rmp$(value);
                      this._products.put_wn2jw4$(objNewKey, obj);
                    }
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else if (refName === _.pcm.util.Constants.Ref_features) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.addFeatures(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL) {
                    this.addAllFeatures(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.removeFeatures(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL) {
                    this.removeAllFeatures();
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                    if (this._features.size() !== 0 && this._features.containsKey_za3rmp$(value)) {
                      var obj_0 = this._features.get_za3rmp$(value);
                      var objNewKey_0 = (obj_0 != null ? obj_0 : Kotlin.throwNPE()).internalGetKey();
                      if (objNewKey_0 == null) {
                        throw new Error('Key newed to null ' + obj_0);
                      }
                      this._features.remove_za3rmp$(value);
                      this._features.put_wn2jw4$(objNewKey_0, obj_0);
                    }
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findProductsByID: function (key) {
            return this._products.get_za3rmp$(key);
          },
          findFeaturesByID: function (key) {
            return this._features.get_za3rmp$(key);
          },
          findByID: function (relationName, idP) {
            {
              if (relationName === _.pcm.util.Constants.Ref_products) {
                return this.findProductsByID(idP);
              }
               else if (relationName === _.pcm.util.Constants.Ref_features) {
                return this.findFeaturesByID(idP);
              }
               else {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            if (containedReference) {
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_products, _.pcm.util.Constants.pcm_Product)) {
                {
                  var tmp$0 = this._products.keySet().iterator();
                  while (tmp$0.hasNext()) {
                    var KMFLoopEntryKey = tmp$0.next();
                    this.internal_visit(visitor, this._products.get_za3rmp$(KMFLoopEntryKey), recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_products);
                  }
                }
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_products);
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_features, _.pcm.util.Constants.pcm_AbstractFeature)) {
                {
                  var tmp$1 = this._features.keySet().iterator();
                  while (tmp$1.hasNext()) {
                    var KMFLoopEntryKey_0 = tmp$1.next();
                    this.internal_visit(visitor, this._features.get_za3rmp$(KMFLoopEntryKey_0), recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_features);
                  }
                }
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_features);
            }
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.name, _.pcm.util.Constants.Att_name, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_PCM;
          }
        }),
        StringValueImpl: Kotlin.createClass(function () {
          return [_.pcm.StringValue, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_fnfad2$ = null;
          this.$internal_containmentRefName_acalve$ = null;
          this.$internal_unsetCmd_hto6ej$ = null;
          this.$internal_readOnlyElem_w4dgrv$ = false;
          this.$internal_recursive_readOnlyElem_gq2jdk$ = false;
          this.$internal_inboundReferences_drxedj$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_rqtw15$ = false;
          this.$internal_is_deleted_cuoihq$ = false;
          this.$is_root_xgxxgv$ = false;
          this.$path_cache_y5o9hs$ = null;
          this.$key_cache_vkj8ue$ = null;
          this.$generated_KMF_ID_rl8hda$ = '' + Math.random() + (new Date()).getTime();
          this.$value_5jo9c9$ = _.pcm.util.Constants.STRING_DEFAULTVAL;
        }, /** @lends _.pcm.impl.StringValueImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_fnfad2$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_fnfad2$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_acalve$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_acalve$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_hto6ej$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_hto6ej$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_w4dgrv$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_w4dgrv$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_gq2jdk$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_gq2jdk$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_drxedj$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_drxedj$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_rqtw15$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_rqtw15$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_cuoihq$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_cuoihq$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_xgxxgv$;
            },
            set: function (tmp$0) {
              this.$is_root_xgxxgv$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_y5o9hs$;
            },
            set: function (tmp$0) {
              this.$path_cache_y5o9hs$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_vkj8ue$;
            },
            set: function (tmp$0) {
              this.$key_cache_vkj8ue$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_rl8hda$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_rl8hda$;
                this.$generated_KMF_ID_rl8hda$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          withValue: function (p) {
            this.value = p;
            return this;
          },
          value: {
            get: function () {
              return this.$value_5jo9c9$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.value)) {
                var kmf_previousVal = this.$value_5jo9c9$;
                this.$value_5jo9c9$ = iP;
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Att_value) {
                this.value = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.value, _.pcm.util.Constants.Att_value, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_StringValue;
          }
        }),
        DateValueImpl: Kotlin.createClass(function () {
          return [_.pcm.DateValue, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_kgkxqf$ = null;
          this.$internal_containmentRefName_ya9nhz$ = null;
          this.$internal_unsetCmd_kqr5m6$ = null;
          this.$internal_readOnlyElem_7qnvyq$ = false;
          this.$internal_recursive_readOnlyElem_apx6n9$ = false;
          this.$internal_inboundReferences_vc0sro$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_mlgqsm$ = false;
          this.$internal_is_deleted_n9bplr$ = false;
          this.$is_root_l6z366$ = false;
          this.$path_cache_he64pf$ = null;
          this.$key_cache_15kptv$ = null;
          this.$generated_KMF_ID_rb2otx$ = '' + Math.random() + (new Date()).getTime();
          this.$value_79fuc$ = _.pcm.util.Constants.STRING_DEFAULTVAL;
        }, /** @lends _.pcm.impl.DateValueImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_kgkxqf$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_kgkxqf$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_ya9nhz$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_ya9nhz$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_kqr5m6$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_kqr5m6$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_7qnvyq$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_7qnvyq$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_apx6n9$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_apx6n9$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_vc0sro$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_vc0sro$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_mlgqsm$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_mlgqsm$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_n9bplr$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_n9bplr$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_l6z366$;
            },
            set: function (tmp$0) {
              this.$is_root_l6z366$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_he64pf$;
            },
            set: function (tmp$0) {
              this.$path_cache_he64pf$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_15kptv$;
            },
            set: function (tmp$0) {
              this.$key_cache_15kptv$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_rb2otx$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_rb2otx$;
                this.$generated_KMF_ID_rb2otx$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          withValue: function (p) {
            this.value = p;
            return this;
          },
          value: {
            get: function () {
              return this.$value_79fuc$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.value)) {
                var kmf_previousVal = this.$value_79fuc$;
                this.$value_79fuc$ = iP;
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Att_value) {
                this.value = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.value, _.pcm.util.Constants.Att_value, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_DateValue;
          }
        }),
        FeatureGroupImpl: Kotlin.createClass(function () {
          return [_.pcm.FeatureGroup, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_b4mzg3$ = null;
          this.$internal_containmentRefName_7h673x$ = null;
          this.$internal_unsetCmd_ynq74o$ = null;
          this.$internal_readOnlyElem_jipf3c$ = false;
          this.$internal_recursive_readOnlyElem_jkdg6j$ = false;
          this.$internal_inboundReferences_ki23iu$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_5xrejw$ = false;
          this.$internal_is_deleted_8bw7kr$ = false;
          this.$is_root_fptf58$ = false;
          this.$path_cache_yn29u5$ = null;
          this.$key_cache_ps8uif$ = null;
          this.$name_2cpgtm$ = _.pcm.util.Constants.STRING_DEFAULTVAL;
          this.$generated_KMF_ID_t3ev1r$ = '' + Math.random() + (new Date()).getTime();
          this._subFeatures = new _.java.util.concurrent.ConcurrentHashMap();
        }, /** @lends _.pcm.impl.FeatureGroupImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_b4mzg3$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_b4mzg3$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_7h673x$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_7h673x$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_ynq74o$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_ynq74o$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_jipf3c$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_jipf3c$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_jkdg6j$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_jkdg6j$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_ki23iu$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_ki23iu$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_5xrejw$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_5xrejw$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_8bw7kr$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_8bw7kr$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_fptf58$;
            },
            set: function (tmp$0) {
              this.$is_root_fptf58$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_yn29u5$;
            },
            set: function (tmp$0) {
              this.$path_cache_yn29u5$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_ps8uif$;
            },
            set: function (tmp$0) {
              this.$key_cache_ps8uif$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            {
              var tmp$0 = this.subFeatures.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                el.delete();
              }
            }
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$1;
              ((tmp$1 = this.internal_unsetCmd) != null ? tmp$1 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withName: function (p) {
            this.name = p;
            return this;
          },
          name: {
            get: function () {
              return this.$name_2cpgtm$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.name)) {
                var kmf_previousVal = this.$name_2cpgtm$;
                this.$name_2cpgtm$ = iP;
              }
            }
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_t3ev1r$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_t3ev1r$;
                this.$generated_KMF_ID_t3ev1r$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          subFeatures: {
            get: function () {
              return _.kotlin.toList_h3panj$(this._subFeatures.values());
            },
            set: function (subFeaturesP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (subFeaturesP == null) {
                throw new Kotlin.IllegalArgumentException(_.pcm.util.Constants.LIST_PARAMETER_OF_SET_IS_NULL_EXCEPTION);
              }
              this.internal_subFeatures(subFeaturesP, true, true);
            }
          },
          internal_subFeatures: function (subFeaturesP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this._subFeatures.values(), subFeaturesP)) {
              var kmf_previousVal = this._subFeatures;
              this._subFeatures.clear();
              {
                var tmp$0 = subFeaturesP.iterator();
                while (tmp$0.hasNext()) {
                  var el = tmp$0.next();
                  var elKey = el.internalGetKey();
                  if (elKey == null) {
                    throw new Error(_.pcm.util.Constants.ELEMENT_HAS_NO_KEY_IN_COLLECTION);
                  }
                  this._subFeatures.put_wn2jw4$(elKey != null ? elKey : Kotlin.throwNPE(), el);
                  el.addInboundReference(this, _.pcm.util.Constants.Ref_subFeatures);
                  el.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_subFeatures, el), _.pcm.util.Constants.Ref_subFeatures);
                }
              }
            }
          },
          doAddSubFeatures: function (subFeaturesP) {
            var _key_ = subFeaturesP.internalGetKey();
            if (_key_ == null || Kotlin.equals(_key_, '')) {
              throw new Error(_.pcm.util.Constants.EMPTY_KEY);
            }
            if (!this._subFeatures.containsKey_za3rmp$(_key_)) {
              this._subFeatures.put_wn2jw4$(_key_, subFeaturesP);
              subFeaturesP.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_subFeatures, subFeaturesP), _.pcm.util.Constants.Ref_subFeatures);
              subFeaturesP.addInboundReference(this, _.pcm.util.Constants.Ref_subFeatures);
            }
          },
          addSubFeatures: function (subFeaturesP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            this.doAddSubFeatures(subFeaturesP);
            return this;
          },
          addAllSubFeatures: function (subFeaturesP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            {
              var tmp$0 = subFeaturesP.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                this.doAddSubFeatures(el);
              }
            }
            return this;
          },
          removeSubFeatures: function (subFeaturesP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            if (this._subFeatures.size() !== 0 && this._subFeatures.containsKey_za3rmp$(subFeaturesP.internalGetKey())) {
              this._subFeatures.remove_za3rmp$(subFeaturesP.internalGetKey());
              subFeaturesP.removeInboundReference(this, _.pcm.util.Constants.Ref_subFeatures);
              subFeaturesP.setEContainer(null, null, null);
            }
            return this;
          },
          removeAllSubFeatures: function () {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            var temp_els = this.subFeatures;
            {
              var tmp$0 = temp_els.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                el.removeInboundReference(this, _.pcm.util.Constants.Ref_subFeatures);
                el.setEContainer(null, null, null);
              }
            }
            this._subFeatures.clear();
            return this;
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_name) {
                this.name = value;
              }
               else if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Ref_subFeatures) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.addSubFeatures(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL) {
                    this.addAllSubFeatures(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.removeSubFeatures(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL) {
                    this.removeAllSubFeatures();
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                    if (this._subFeatures.size() !== 0 && this._subFeatures.containsKey_za3rmp$(value)) {
                      var obj = this._subFeatures.get_za3rmp$(value);
                      var objNewKey = (obj != null ? obj : Kotlin.throwNPE()).internalGetKey();
                      if (objNewKey == null) {
                        throw new Error('Key newed to null ' + obj);
                      }
                      this._subFeatures.remove_za3rmp$(value);
                      this._subFeatures.put_wn2jw4$(objNewKey, obj);
                    }
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findSubFeaturesByID: function (key) {
            return this._subFeatures.get_za3rmp$(key);
          },
          findByID: function (relationName, idP) {
            {
              if (relationName === _.pcm.util.Constants.Ref_subFeatures) {
                return this.findSubFeaturesByID(idP);
              }
               else {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            if (containedReference) {
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_subFeatures, _.pcm.util.Constants.pcm_AbstractFeature)) {
                {
                  var tmp$0 = this._subFeatures.keySet().iterator();
                  while (tmp$0.hasNext()) {
                    var KMFLoopEntryKey = tmp$0.next();
                    this.internal_visit(visitor, this._subFeatures.get_za3rmp$(KMFLoopEntryKey), recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_subFeatures);
                  }
                }
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_subFeatures);
            }
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.name, _.pcm.util.Constants.Att_name, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_FeatureGroup;
          }
        }),
        PartialImpl: Kotlin.createClass(function () {
          return [_.pcm.Partial, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_bphl93$ = null;
          this.$internal_containmentRefName_p33ucn$ = null;
          this.$internal_unsetCmd_ahlgac$ = null;
          this.$internal_readOnlyElem_7ut2fg$ = false;
          this.$internal_recursive_readOnlyElem_mdcq7t$ = false;
          this.$internal_inboundReferences_qbpe4a$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_j2u6pk$ = false;
          this.$internal_is_deleted_8wqtdr$ = false;
          this.$is_root_636wj4$ = false;
          this.$path_cache_2qal69$ = null;
          this.$key_cache_oitcnv$ = null;
          this.$generated_KMF_ID_v9bx0t$ = '' + Math.random() + (new Date()).getTime();
          this.$value_bxh2i$ = null;
        }, /** @lends _.pcm.impl.PartialImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_bphl93$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_bphl93$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_p33ucn$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_p33ucn$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_ahlgac$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_ahlgac$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_7ut2fg$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_7ut2fg$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_mdcq7t$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_mdcq7t$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_qbpe4a$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_qbpe4a$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_j2u6pk$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_j2u6pk$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_8wqtdr$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_8wqtdr$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_636wj4$;
            },
            set: function (tmp$0) {
              this.$is_root_636wj4$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_2qal69$;
            },
            set: function (tmp$0) {
              this.$path_cache_2qal69$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_oitcnv$;
            },
            set: function (tmp$0) {
              this.$key_cache_oitcnv$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            if (this.value != null) {
              var tmp$0;
              ((tmp$0 = this.value) != null ? tmp$0 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_value);
              this.value = null;
            }
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$1;
              ((tmp$1 = this.internal_unsetCmd) != null ? tmp$1 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_v9bx0t$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_v9bx0t$;
                this.$generated_KMF_ID_v9bx0t$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          value: {
            get: function () {
              return this.$value_bxh2i$;
            },
            set: function (valueP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              this.internal_value(valueP, true, true);
            }
          },
          internal_value: function (valueP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this.$value_bxh2i$, valueP)) {
              if (this.$value_bxh2i$ != null) {
                var tmp$0;
                ((tmp$0 = this.$value_bxh2i$) != null ? tmp$0 : Kotlin.throwNPE()).setEContainer(null, null, null);
              }
              if (valueP != null) {
                (valueP != null ? valueP : Kotlin.throwNPE()).setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.SET, _.pcm.util.Constants.Ref_value, null), _.pcm.util.Constants.Ref_value);
              }
              var kmf_previousVal = this.$value_bxh2i$;
              if (valueP != null) {
                (valueP != null ? valueP : Kotlin.throwNPE()).addInboundReference(this, _.pcm.util.Constants.Ref_value);
              }
               else {
                if (this.$value_bxh2i$ != null) {
                  var tmp$1;
                  ((tmp$1 = this.$value_bxh2i$) != null ? tmp$1 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_value);
                }
              }
              this.$value_bxh2i$ = valueP;
            }
          },
          withValue: function (ref) {
            return this;
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Ref_value) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.SET) {
                    this.value = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.value = null;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.value = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              if (relationName === _.pcm.util.Constants.Ref_value) {
                var objFound = this.value;
                if (objFound != null && Kotlin.equals(objFound.internalGetKey(), idP)) {
                  return objFound;
                }
                 else {
                  return null;
                }
              }
               else {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            if (containedReference) {
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_value, _.pcm.util.Constants.pcm_Value)) {
                this.internal_visit(visitor, this.value, recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_value);
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_value);
            }
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Partial;
          }
        }),
        NotApplicableImpl: Kotlin.createClass(function () {
          return [_.pcm.NotApplicable, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_5n0rzc$ = null;
          this.$internal_containmentRefName_weyf9k$ = null;
          this.$internal_unsetCmd_uw87mr$ = null;
          this.$internal_readOnlyElem_j874t$ = false;
          this.$internal_recursive_readOnlyElem_4n8he2$ = false;
          this.$internal_inboundReferences_oiqbf9$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_tvu1zd$ = false;
          this.$internal_is_deleted_2ua040$ = false;
          this.$is_root_4iywbl$ = false;
          this.$path_cache_gn4h9a$ = null;
          this.$key_cache_2cjces$ = null;
          this.$generated_KMF_ID_hilr9g$ = '' + Math.random() + (new Date()).getTime();
        }, /** @lends _.pcm.impl.NotApplicableImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_5n0rzc$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_5n0rzc$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_weyf9k$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_weyf9k$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_uw87mr$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_uw87mr$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_j874t$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_j874t$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_4n8he2$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_4n8he2$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_oiqbf9$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_oiqbf9$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_tvu1zd$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_tvu1zd$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_2ua040$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_2ua040$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_4iywbl$;
            },
            set: function (tmp$0) {
              this.$is_root_4iywbl$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_gn4h9a$;
            },
            set: function (tmp$0) {
              this.$path_cache_gn4h9a$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_2cjces$;
            },
            set: function (tmp$0) {
              this.$key_cache_2cjces$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_hilr9g$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_hilr9g$;
                this.$generated_KMF_ID_hilr9g$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_NotApplicable;
          }
        }),
        RealValueImpl: Kotlin.createClass(function () {
          return [_.pcm.RealValue, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_de09xl$ = null;
          this.$internal_containmentRefName_m5kvrt$ = null;
          this.$internal_unsetCmd_2i5nlu$ = null;
          this.$internal_readOnlyElem_abzczm$ = false;
          this.$internal_recursive_readOnlyElem_wt23th$ = false;
          this.$internal_inboundReferences_o4me0c$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_61z0ye$ = false;
          this.$internal_is_deleted_al9i29$ = false;
          this.$is_root_8k6qf6$ = false;
          this.$path_cache_404ucd$ = null;
          this.$key_cache_wjjveb$ = null;
          this.$generated_KMF_ID_k7zgl$ = '' + Math.random() + (new Date()).getTime();
          this.$value_zb2tgc$ = _.pcm.util.Constants.DOUBLE_DEFAULTVAL;
        }, /** @lends _.pcm.impl.RealValueImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_de09xl$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_de09xl$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_m5kvrt$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_m5kvrt$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_2i5nlu$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_2i5nlu$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_abzczm$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_abzczm$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_wt23th$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_wt23th$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_o4me0c$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_o4me0c$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_61z0ye$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_61z0ye$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_al9i29$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_al9i29$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_8k6qf6$;
            },
            set: function (tmp$0) {
              this.$is_root_8k6qf6$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_404ucd$;
            },
            set: function (tmp$0) {
              this.$path_cache_404ucd$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_wjjveb$;
            },
            set: function (tmp$0) {
              this.$key_cache_wjjveb$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_k7zgl$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_k7zgl$;
                this.$generated_KMF_ID_k7zgl$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          withValue: function (p) {
            this.value = p;
            return this;
          },
          value: {
            get: function () {
              return this.$value_zb2tgc$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (iP !== this.value) {
                var kmf_previousVal = this.$value_zb2tgc$;
                this.$value_zb2tgc$ = iP;
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Att_value) {
                this.value = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.value, _.pcm.util.Constants.Att_value, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_RealValue;
          }
        }),
        BooleanValueImpl: Kotlin.createClass(function () {
          return [_.pcm.BooleanValue, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_28cvf1$ = null;
          this.$internal_containmentRefName_inmhs3$ = null;
          this.$internal_unsetCmd_vp8y20$ = null;
          this.$internal_readOnlyElem_yu7lug$ = false;
          this.$internal_recursive_readOnlyElem_c5d73f$ = false;
          this.$internal_inboundReferences_x1i3e2$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_4yd95g$ = false;
          this.$internal_is_deleted_513nad$ = false;
          this.$is_root_q1idws$ = false;
          this.$path_cache_bryhxp$ = null;
          this.$key_cache_3lx7jr$ = null;
          this.$generated_KMF_ID_7eyonl$ = '' + Math.random() + (new Date()).getTime();
          this.$value_2dbhve$ = _.pcm.util.Constants.BOOLEAN_DEFAULTVAL;
        }, /** @lends _.pcm.impl.BooleanValueImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_28cvf1$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_28cvf1$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_inmhs3$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_inmhs3$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_vp8y20$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_vp8y20$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_yu7lug$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_yu7lug$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_c5d73f$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_c5d73f$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_x1i3e2$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_x1i3e2$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_4yd95g$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_4yd95g$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_513nad$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_513nad$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_q1idws$;
            },
            set: function (tmp$0) {
              this.$is_root_q1idws$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_bryhxp$;
            },
            set: function (tmp$0) {
              this.$path_cache_bryhxp$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_3lx7jr$;
            },
            set: function (tmp$0) {
              this.$key_cache_3lx7jr$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_7eyonl$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_7eyonl$;
                this.$generated_KMF_ID_7eyonl$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          withValue: function (p) {
            this.value = p;
            return this;
          },
          value: {
            get: function () {
              return this.$value_2dbhve$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.value)) {
                var kmf_previousVal = this.$value_2dbhve$;
                this.$value_2dbhve$ = iP;
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Att_value) {
                this.value = Kotlin.equals('true', value) || Kotlin.equals(true, value);
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.value, _.pcm.util.Constants.Att_value, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_BooleanValue;
          }
        }),
        AbstractFeatureImpl: Kotlin.createClass(function () {
          return [_.pcm.AbstractFeature, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_shvw3a$ = null;
          this.$internal_containmentRefName_h0owpy$ = null;
          this.$internal_unsetCmd_qn6bcx$ = null;
          this.$internal_readOnlyElem_bt6y0v$ = false;
          this.$internal_recursive_readOnlyElem_6nmx1w$ = false;
          this.$internal_inboundReferences_9cp9u5$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_7vav2t$ = false;
          this.$internal_is_deleted_vamnym$ = false;
          this.$is_root_cpzoeb$ = false;
          this.$path_cache_m1d8u4$ = null;
          this.$key_cache_bc7z02$ = null;
          this.$name_qsmf1b$ = _.pcm.util.Constants.STRING_DEFAULTVAL;
          this.$generated_KMF_ID_ja4s7u$ = '' + Math.random() + (new Date()).getTime();
        }, /** @lends _.pcm.impl.AbstractFeatureImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_shvw3a$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_shvw3a$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_h0owpy$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_h0owpy$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_qn6bcx$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_qn6bcx$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_bt6y0v$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_bt6y0v$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_6nmx1w$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_6nmx1w$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_9cp9u5$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_9cp9u5$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_7vav2t$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_7vav2t$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_vamnym$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_vamnym$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_cpzoeb$;
            },
            set: function (tmp$0) {
              this.$is_root_cpzoeb$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_m1d8u4$;
            },
            set: function (tmp$0) {
              this.$path_cache_m1d8u4$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_bc7z02$;
            },
            set: function (tmp$0) {
              this.$key_cache_bc7z02$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withName: function (p) {
            this.name = p;
            return this;
          },
          name: {
            get: function () {
              return this.$name_qsmf1b$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.name)) {
                var kmf_previousVal = this.$name_qsmf1b$;
                this.$name_qsmf1b$ = iP;
              }
            }
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_ja4s7u$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_ja4s7u$;
                this.$generated_KMF_ID_ja4s7u$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_name) {
                this.name = value;
              }
               else if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.name, _.pcm.util.Constants.Att_name, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_AbstractFeature;
          }
        }),
        ValueImpl: Kotlin.createClass(function () {
          return [_.pcm.Value, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_50nb21$ = null;
          this.$internal_containmentRefName_r5wxm1$ = null;
          this.$internal_unsetCmd_dikddo$ = null;
          this.$internal_readOnlyElem_cugzr8$ = false;
          this.$internal_recursive_readOnlyElem_c6nt93$ = false;
          this.$internal_inboundReferences_2t0r3e$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_c8o9p4$ = false;
          this.$internal_is_deleted_7te2xd$ = false;
          this.$is_root_z69g74$ = false;
          this.$path_cache_8xj28x$ = null;
          this.$key_cache_sdljjp$ = null;
          this.$generated_KMF_ID_iaa5q5$ = '' + Math.random() + (new Date()).getTime();
        }, /** @lends _.pcm.impl.ValueImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_50nb21$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_50nb21$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_r5wxm1$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_r5wxm1$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_dikddo$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_dikddo$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_cugzr8$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_cugzr8$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_c6nt93$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_c6nt93$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_2t0r3e$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_2t0r3e$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_c8o9p4$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_c8o9p4$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_7te2xd$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_7te2xd$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_z69g74$;
            },
            set: function (tmp$0) {
              this.$is_root_z69g74$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_8xj28x$;
            },
            set: function (tmp$0) {
              this.$path_cache_8xj28x$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_sdljjp$;
            },
            set: function (tmp$0) {
              this.$key_cache_sdljjp$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_iaa5q5$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_iaa5q5$;
                this.$generated_KMF_ID_iaa5q5$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Value;
          }
        }),
        IntegerValueImpl: Kotlin.createClass(function () {
          return [_.pcm.IntegerValue, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_7502ub$ = null;
          this.$internal_containmentRefName_o0v3fx$ = null;
          this.$internal_unsetCmd_92tzte$ = null;
          this.$internal_readOnlyElem_8suvo2$ = false;
          this.$internal_recursive_readOnlyElem_uw2hs1$ = false;
          this.$internal_inboundReferences_x7qi0w$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_ddbwoy$ = false;
          this.$internal_is_deleted_9xqupn$ = false;
          this.$is_root_oieys6$ = false;
          this.$path_cache_2kz6lz$ = null;
          this.$key_cache_oizixt$ = null;
          this.$generated_KMF_ID_rb2v3b$ = '' + Math.random() + (new Date()).getTime();
          this.$value_d0jllc$ = _.pcm.util.Constants.INT_DEFAULTVAL;
        }, /** @lends _.pcm.impl.IntegerValueImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_7502ub$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_7502ub$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_o0v3fx$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_o0v3fx$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_92tzte$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_92tzte$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_8suvo2$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_8suvo2$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_uw2hs1$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_uw2hs1$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_x7qi0w$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_x7qi0w$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_ddbwoy$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_ddbwoy$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_9xqupn$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_9xqupn$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_oieys6$;
            },
            set: function (tmp$0) {
              this.$is_root_oieys6$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_2kz6lz$;
            },
            set: function (tmp$0) {
              this.$path_cache_2kz6lz$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_oizixt$;
            },
            set: function (tmp$0) {
              this.$key_cache_oizixt$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_rb2v3b$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_rb2v3b$;
                this.$generated_KMF_ID_rb2v3b$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          withValue: function (p) {
            this.value = p;
            return this;
          },
          value: {
            get: function () {
              return this.$value_d0jllc$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (iP !== this.value) {
                var kmf_previousVal = this.$value_d0jllc$;
                this.$value_d0jllc$ = iP;
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Att_value) {
                this.value = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.value, _.pcm.util.Constants.Att_value, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_IntegerValue;
          }
        }),
        ConditionalImpl: Kotlin.createClass(function () {
          return [_.pcm.Conditional, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_hmg0tg$ = null;
          this.$internal_containmentRefName_fbmlqs$ = null;
          this.$internal_unsetCmd_wwxysv$ = null;
          this.$internal_readOnlyElem_jk8p5d$ = false;
          this.$internal_recursive_readOnlyElem_orxvb6$ = false;
          this.$internal_inboundReferences_tnjzq9$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_ymrsfx$ = false;
          this.$internal_is_deleted_kf6sos$ = false;
          this.$is_root_oz08d1$ = false;
          this.$path_cache_hqkiay$ = null;
          this.$key_cache_tt450g$ = null;
          this.$generated_KMF_ID_5bxatk$ = '' + Math.random() + (new Date()).getTime();
          this.$condition_gl1hif$ = null;
          this.$value_3p96z5$ = null;
        }, /** @lends _.pcm.impl.ConditionalImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_hmg0tg$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_hmg0tg$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_fbmlqs$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_fbmlqs$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_wwxysv$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_wwxysv$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_jk8p5d$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_jk8p5d$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_orxvb6$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_orxvb6$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_tnjzq9$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_tnjzq9$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_ymrsfx$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_ymrsfx$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_kf6sos$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_kf6sos$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_oz08d1$;
            },
            set: function (tmp$0) {
              this.$is_root_oz08d1$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_hqkiay$;
            },
            set: function (tmp$0) {
              this.$path_cache_hqkiay$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_tt450g$;
            },
            set: function (tmp$0) {
              this.$key_cache_tt450g$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            if (this.value != null) {
              var tmp$0;
              ((tmp$0 = this.value) != null ? tmp$0 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_value);
              this.value = null;
            }
            if (this.condition != null) {
              var tmp$1;
              ((tmp$1 = this.condition) != null ? tmp$1 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_condition);
              this.condition = null;
            }
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$2;
              ((tmp$2 = this.internal_unsetCmd) != null ? tmp$2 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_5bxatk$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_5bxatk$;
                this.$generated_KMF_ID_5bxatk$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          condition: {
            get: function () {
              return this.$condition_gl1hif$;
            },
            set: function (conditionP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              this.internal_condition(conditionP, true, true);
            }
          },
          internal_condition: function (conditionP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this.$condition_gl1hif$, conditionP)) {
              if (this.$condition_gl1hif$ != null) {
                var tmp$0;
                ((tmp$0 = this.$condition_gl1hif$) != null ? tmp$0 : Kotlin.throwNPE()).setEContainer(null, null, null);
              }
              if (conditionP != null) {
                (conditionP != null ? conditionP : Kotlin.throwNPE()).setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.SET, _.pcm.util.Constants.Ref_condition, null), _.pcm.util.Constants.Ref_condition);
              }
              var kmf_previousVal = this.$condition_gl1hif$;
              if (conditionP != null) {
                (conditionP != null ? conditionP : Kotlin.throwNPE()).addInboundReference(this, _.pcm.util.Constants.Ref_condition);
              }
               else {
                if (this.$condition_gl1hif$ != null) {
                  var tmp$1;
                  ((tmp$1 = this.$condition_gl1hif$) != null ? tmp$1 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_condition);
                }
              }
              this.$condition_gl1hif$ = conditionP;
            }
          },
          withCondition: function (ref) {
            return this;
          },
          value: {
            get: function () {
              return this.$value_3p96z5$;
            },
            set: function (valueP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              this.internal_value(valueP, true, true);
            }
          },
          internal_value: function (valueP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this.$value_3p96z5$, valueP)) {
              if (this.$value_3p96z5$ != null) {
                var tmp$0;
                ((tmp$0 = this.$value_3p96z5$) != null ? tmp$0 : Kotlin.throwNPE()).setEContainer(null, null, null);
              }
              if (valueP != null) {
                (valueP != null ? valueP : Kotlin.throwNPE()).setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.SET, _.pcm.util.Constants.Ref_value, null), _.pcm.util.Constants.Ref_value);
              }
              var kmf_previousVal = this.$value_3p96z5$;
              if (valueP != null) {
                (valueP != null ? valueP : Kotlin.throwNPE()).addInboundReference(this, _.pcm.util.Constants.Ref_value);
              }
               else {
                if (this.$value_3p96z5$ != null) {
                  var tmp$1;
                  ((tmp$1 = this.$value_3p96z5$) != null ? tmp$1 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_value);
                }
              }
              this.$value_3p96z5$ = valueP;
            }
          },
          withValue: function (ref) {
            return this;
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Ref_value) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.SET) {
                    this.value = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.value = null;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.value = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else if (refName === _.pcm.util.Constants.Ref_condition) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.SET) {
                    this.condition = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.condition = null;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.condition = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              if (relationName === _.pcm.util.Constants.Ref_value) {
                var objFound = this.value;
                if (objFound != null && Kotlin.equals(objFound.internalGetKey(), idP)) {
                  return objFound;
                }
                 else {
                  return null;
                }
              }
               else if (relationName === _.pcm.util.Constants.Ref_condition) {
                var objFound_0 = this.condition;
                if (objFound_0 != null && Kotlin.equals(objFound_0.internalGetKey(), idP)) {
                  return objFound_0;
                }
                 else {
                  return null;
                }
              }
               else {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            if (containedReference) {
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_value, _.pcm.util.Constants.pcm_Value)) {
                this.internal_visit(visitor, this.value, recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_value);
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_value);
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_condition, _.pcm.util.Constants.pcm_Value)) {
                this.internal_visit(visitor, this.condition, recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_condition);
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_condition);
            }
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Conditional;
          }
        }),
        ProductImpl: Kotlin.createClass(function () {
          return [_.pcm.Product, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_z1voor$ = null;
          this.$internal_containmentRefName_vvecdn$ = null;
          this.$internal_unsetCmd_wmyax2$ = null;
          this.$internal_readOnlyElem_xtpgmy$ = false;
          this.$internal_recursive_readOnlyElem_yt8sex$ = false;
          this.$internal_inboundReferences_hnwdnc$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_twsw4a$ = false;
          this.$internal_is_deleted_x6hlf1$ = false;
          this.$is_root_p6goce$ = false;
          this.$path_cache_8qlhr5$ = null;
          this.$key_cache_r6mdzd$ = null;
          this.$name_1m7wck$ = _.pcm.util.Constants.STRING_DEFAULTVAL;
          this.$generated_KMF_ID_vbjwe9$ = '' + Math.random() + (new Date()).getTime();
          this._values = new _.java.util.concurrent.ConcurrentHashMap();
        }, /** @lends _.pcm.impl.ProductImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_z1voor$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_z1voor$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_vvecdn$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_vvecdn$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_wmyax2$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_wmyax2$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_xtpgmy$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_xtpgmy$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_yt8sex$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_yt8sex$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_hnwdnc$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_hnwdnc$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_twsw4a$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_twsw4a$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_x6hlf1$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_x6hlf1$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_p6goce$;
            },
            set: function (tmp$0) {
              this.$is_root_p6goce$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_8qlhr5$;
            },
            set: function (tmp$0) {
              this.$path_cache_8qlhr5$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_r6mdzd$;
            },
            set: function (tmp$0) {
              this.$key_cache_r6mdzd$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            {
              var tmp$0 = this.values.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                el.delete();
              }
            }
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$1;
              ((tmp$1 = this.internal_unsetCmd) != null ? tmp$1 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withName: function (p) {
            this.name = p;
            return this;
          },
          name: {
            get: function () {
              return this.$name_1m7wck$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.name)) {
                var kmf_previousVal = this.$name_1m7wck$;
                this.$name_1m7wck$ = iP;
              }
            }
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_vbjwe9$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_vbjwe9$;
                this.$generated_KMF_ID_vbjwe9$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          values: {
            get: function () {
              return _.kotlin.toList_h3panj$(this._values.values());
            },
            set: function (valuesP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (valuesP == null) {
                throw new Kotlin.IllegalArgumentException(_.pcm.util.Constants.LIST_PARAMETER_OF_SET_IS_NULL_EXCEPTION);
              }
              this.internal_values(valuesP, true, true);
            }
          },
          internal_values: function (valuesP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this._values.values(), valuesP)) {
              var kmf_previousVal = this._values;
              this._values.clear();
              {
                var tmp$0 = valuesP.iterator();
                while (tmp$0.hasNext()) {
                  var el = tmp$0.next();
                  var elKey = el.internalGetKey();
                  if (elKey == null) {
                    throw new Error(_.pcm.util.Constants.ELEMENT_HAS_NO_KEY_IN_COLLECTION);
                  }
                  this._values.put_wn2jw4$(elKey != null ? elKey : Kotlin.throwNPE(), el);
                  el.addInboundReference(this, _.pcm.util.Constants.Ref_values);
                  el.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_values, el), _.pcm.util.Constants.Ref_values);
                }
              }
            }
          },
          doAddValues: function (valuesP) {
            var _key_ = valuesP.internalGetKey();
            if (_key_ == null || Kotlin.equals(_key_, '')) {
              throw new Error(_.pcm.util.Constants.EMPTY_KEY);
            }
            if (!this._values.containsKey_za3rmp$(_key_)) {
              this._values.put_wn2jw4$(_key_, valuesP);
              valuesP.setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.REMOVE, _.pcm.util.Constants.Ref_values, valuesP), _.pcm.util.Constants.Ref_values);
              valuesP.addInboundReference(this, _.pcm.util.Constants.Ref_values);
            }
          },
          addValues: function (valuesP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            this.doAddValues(valuesP);
            return this;
          },
          addAllValues: function (valuesP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            {
              var tmp$0 = valuesP.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                this.doAddValues(el);
              }
            }
            return this;
          },
          removeValues: function (valuesP) {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            if (this._values.size() !== 0 && this._values.containsKey_za3rmp$(valuesP.internalGetKey())) {
              this._values.remove_za3rmp$(valuesP.internalGetKey());
              valuesP.removeInboundReference(this, _.pcm.util.Constants.Ref_values);
              valuesP.setEContainer(null, null, null);
            }
            return this;
          },
          removeAllValues: function () {
            if (this.isReadOnly()) {
              throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
            }
            var temp_els = this.values;
            {
              var tmp$0 = temp_els.iterator();
              while (tmp$0.hasNext()) {
                var el = tmp$0.next();
                el.removeInboundReference(this, _.pcm.util.Constants.Ref_values);
                el.setEContainer(null, null, null);
              }
            }
            this._values.clear();
            return this;
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_name) {
                this.name = value;
              }
               else if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Ref_values) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.addValues(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL) {
                    this.addAllValues(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.removeValues(value != null ? value : Kotlin.throwNPE());
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL) {
                    this.removeAllValues();
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                    if (this._values.size() !== 0 && this._values.containsKey_za3rmp$(value)) {
                      var obj = this._values.get_za3rmp$(value);
                      var objNewKey = (obj != null ? obj : Kotlin.throwNPE()).internalGetKey();
                      if (objNewKey == null) {
                        throw new Error('Key newed to null ' + obj);
                      }
                      this._values.remove_za3rmp$(value);
                      this._values.put_wn2jw4$(objNewKey, obj);
                    }
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findValuesByID: function (key) {
            return this._values.get_za3rmp$(key);
          },
          findByID: function (relationName, idP) {
            {
              if (relationName === _.pcm.util.Constants.Ref_values) {
                return this.findValuesByID(idP);
              }
               else {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            if (containedReference) {
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_values, _.pcm.util.Constants.pcm_Cell)) {
                {
                  var tmp$0 = this._values.keySet().iterator();
                  while (tmp$0.hasNext()) {
                    var KMFLoopEntryKey = tmp$0.next();
                    this.internal_visit(visitor, this._values.get_za3rmp$(KMFLoopEntryKey), recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_values);
                  }
                }
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_values);
            }
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
            visitor.visit(this.name, _.pcm.util.Constants.Att_name, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Product;
          }
        }),
        UnitImpl: Kotlin.createClass(function () {
          return [_.pcm.Unit, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_muntce$ = null;
          this.$internal_containmentRefName_kfw5r6$ = null;
          this.$internal_unsetCmd_6k5g8t$ = null;
          this.$internal_readOnlyElem_8q17lp$ = false;
          this.$internal_recursive_readOnlyElem_guzosw$ = false;
          this.$internal_inboundReferences_e1ng29$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_ziie27$ = false;
          this.$internal_is_deleted_k1x1h2$ = false;
          this.$is_root_q51e7t$ = false;
          this.$path_cache_irl8u0$ = null;
          this.$key_cache_4le3rm$ = null;
          this.$generated_KMF_ID_d1zrva$ = '' + Math.random() + (new Date()).getTime();
          this.$unit_74832s$ = _.pcm.util.Constants.STRING_DEFAULTVAL;
          this.$value_7jy1k1$ = null;
        }, /** @lends _.pcm.impl.UnitImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_muntce$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_muntce$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_kfw5r6$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_kfw5r6$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_6k5g8t$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_6k5g8t$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_8q17lp$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_8q17lp$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_guzosw$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_guzosw$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_e1ng29$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_e1ng29$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_ziie27$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_ziie27$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_k1x1h2$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_k1x1h2$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_q51e7t$;
            },
            set: function (tmp$0) {
              this.$is_root_q51e7t$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_irl8u0$;
            },
            set: function (tmp$0) {
              this.$path_cache_irl8u0$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_4le3rm$;
            },
            set: function (tmp$0) {
              this.$key_cache_4le3rm$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            if (this.value != null) {
              var tmp$0;
              ((tmp$0 = this.value) != null ? tmp$0 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_value);
              this.value = null;
            }
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$1;
              ((tmp$1 = this.internal_unsetCmd) != null ? tmp$1 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_d1zrva$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_d1zrva$;
                this.$generated_KMF_ID_d1zrva$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          withUnit: function (p) {
            this.unit = p;
            return this;
          },
          unit: {
            get: function () {
              return this.$unit_74832s$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.unit)) {
                var kmf_previousVal = this.$unit_74832s$;
                this.$unit_74832s$ = iP;
              }
            }
          },
          value: {
            get: function () {
              return this.$value_7jy1k1$;
            },
            set: function (valueP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              this.internal_value(valueP, true, true);
            }
          },
          internal_value: function (valueP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this.$value_7jy1k1$, valueP)) {
              if (this.$value_7jy1k1$ != null) {
                var tmp$0;
                ((tmp$0 = this.$value_7jy1k1$) != null ? tmp$0 : Kotlin.throwNPE()).setEContainer(null, null, null);
              }
              if (valueP != null) {
                (valueP != null ? valueP : Kotlin.throwNPE()).setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.SET, _.pcm.util.Constants.Ref_value, null), _.pcm.util.Constants.Ref_value);
              }
              var kmf_previousVal = this.$value_7jy1k1$;
              if (valueP != null) {
                (valueP != null ? valueP : Kotlin.throwNPE()).addInboundReference(this, _.pcm.util.Constants.Ref_value);
              }
               else {
                if (this.$value_7jy1k1$ != null) {
                  var tmp$1;
                  ((tmp$1 = this.$value_7jy1k1$) != null ? tmp$1 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_value);
                }
              }
              this.$value_7jy1k1$ = valueP;
            }
          },
          withValue: function (ref) {
            return this;
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Att_unit) {
                this.unit = value;
              }
               else if (refName === _.pcm.util.Constants.Ref_value) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.SET) {
                    this.value = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.value = null;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.value = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              if (relationName === _.pcm.util.Constants.Ref_value) {
                var objFound = this.value;
                if (objFound != null && Kotlin.equals(objFound.internalGetKey(), idP)) {
                  return objFound;
                }
                 else {
                  return null;
                }
              }
               else {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            if (containedReference) {
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_value, _.pcm.util.Constants.pcm_Value)) {
                this.internal_visit(visitor, this.value, recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_value);
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_value);
            }
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.unit, _.pcm.util.Constants.Att_unit, this);
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Unit;
          }
        }),
        NotAvailableImpl: Kotlin.createClass(function () {
          return [_.pcm.NotAvailable, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_hpco3k$ = null;
          this.$internal_containmentRefName_nh4az4$ = null;
          this.$internal_unsetCmd_l37d57$ = null;
          this.$internal_readOnlyElem_czva7p$ = false;
          this.$internal_recursive_readOnlyElem_e9iuu6$ = false;
          this.$internal_inboundReferences_x73kl9$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_mjaujj$ = false;
          this.$internal_is_deleted_ki3fyw$ = false;
          this.$is_root_k9elc7$ = false;
          this.$path_cache_axqn7e$ = null;
          this.$key_cache_sfxef0$ = null;
          this.$generated_KMF_ID_qd5zto$ = '' + Math.random() + (new Date()).getTime();
        }, /** @lends _.pcm.impl.NotAvailableImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_hpco3k$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_hpco3k$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_nh4az4$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_nh4az4$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_l37d57$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_l37d57$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_czva7p$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_czva7p$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_e9iuu6$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_e9iuu6$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_x73kl9$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_x73kl9$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_mjaujj$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_mjaujj$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_ki3fyw$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_ki3fyw$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_k9elc7$;
            },
            set: function (tmp$0) {
              this.$is_root_k9elc7$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_axqn7e$;
            },
            set: function (tmp$0) {
              this.$path_cache_axqn7e$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_sfxef0$;
            },
            set: function (tmp$0) {
              this.$key_cache_sfxef0$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_qd5zto$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_qd5zto$;
                this.$generated_KMF_ID_qd5zto$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_NotAvailable;
          }
        }),
        DimensionImpl: Kotlin.createClass(function () {
          return [_.pcm.Dimension, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_9i1mik$ = null;
          this.$internal_containmentRefName_w8gz78$ = null;
          this.$internal_unsetCmd_bdlxjj$ = null;
          this.$internal_readOnlyElem_i30fjl$ = false;
          this.$internal_recursive_readOnlyElem_2i9h26$ = false;
          this.$internal_inboundReferences_m8gapt$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_drrnkt$ = false;
          this.$internal_is_deleted_6paun8$ = false;
          this.$is_root_ft0xfv$ = false;
          this.$path_cache_lv28ne$ = null;
          this.$key_cache_91xqtc$ = null;
          this.$generated_KMF_ID_caypnc$ = '' + Math.random() + (new Date()).getTime();
        }, /** @lends _.pcm.impl.DimensionImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_9i1mik$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_9i1mik$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_w8gz78$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_w8gz78$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_bdlxjj$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_bdlxjj$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_i30fjl$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_i30fjl$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_2i9h26$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_2i9h26$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_m8gapt$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_m8gapt$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_drrnkt$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_drrnkt$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_6paun8$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_6paun8$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_ft0xfv$;
            },
            set: function (tmp$0) {
              this.$is_root_ft0xfv$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_lv28ne$;
            },
            set: function (tmp$0) {
              this.$path_cache_lv28ne$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_91xqtc$;
            },
            set: function (tmp$0) {
              this.$key_cache_91xqtc$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_caypnc$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_caypnc$;
                this.$generated_KMF_ID_caypnc$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Dimension;
          }
        }),
        VersionImpl: Kotlin.createClass(function () {
          return [_.pcm.Version, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_a1mq4y$ = null;
          this.$internal_containmentRefName_wfp67y$ = null;
          this.$internal_unsetCmd_4g7ul9$ = null;
          this.$internal_readOnlyElem_e0aual$ = false;
          this.$internal_recursive_readOnlyElem_4baik0$ = false;
          this.$internal_inboundReferences_gx7x81$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_5tlsa9$ = false;
          this.$internal_is_deleted_cudi0a$ = false;
          this.$is_root_9ypmef$ = false;
          this.$path_cache_mmltrc$ = null;
          this.$key_cache_xeh8y6$ = null;
          this.$generated_KMF_ID_uethxm$ = '' + Math.random() + (new Date()).getTime();
        }, /** @lends _.pcm.impl.VersionImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_a1mq4y$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_a1mq4y$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_wfp67y$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_wfp67y$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_4g7ul9$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_4g7ul9$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_e0aual$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_e0aual$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_4baik0$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_4baik0$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_gx7x81$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_gx7x81$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_5tlsa9$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_5tlsa9$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_cudi0a$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_cudi0a$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_9ypmef$;
            },
            set: function (tmp$0) {
              this.$is_root_9ypmef$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_mmltrc$;
            },
            set: function (tmp$0) {
              this.$path_cache_mmltrc$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_xeh8y6$;
            },
            set: function (tmp$0) {
              this.$key_cache_xeh8y6$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$0;
              ((tmp$0 = this.internal_unsetCmd) != null ? tmp$0 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_uethxm$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_uethxm$;
                this.$generated_KMF_ID_uethxm$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Version;
          }
        }),
        CellImpl: Kotlin.createClass(function () {
          return [_.pcm.Cell, _.pcm.container.KMFContainerImpl];
        }, function () {
          this.$internal_eContainer_itq4vg$ = null;
          this.$internal_containmentRefName_ku82f8$ = null;
          this.$internal_unsetCmd_goudjz$ = null;
          this.$internal_readOnlyElem_t03kpt$ = false;
          this.$internal_recursive_readOnlyElem_6vvxji$ = false;
          this.$internal_inboundReferences_piju7j$ = new Kotlin.ComplexHashMap();
          this.$internal_deleteInProgress_juib5f$ = false;
          this.$internal_is_deleted_g0zd04$ = false;
          this.$is_root_ds72hn$ = false;
          this.$path_cache_ezsujq$ = null;
          this.$key_cache_h93ze8$ = null;
          this.$content_whd7zt$ = _.pcm.util.Constants.STRING_DEFAULTVAL;
          this.$generated_KMF_ID_ln2zpk$ = '' + Math.random() + (new Date()).getTime();
          this.$feature_6m8eys$ = null;
          this.$interpretation_ddutmu$ = null;
        }, /** @lends _.pcm.impl.CellImpl.prototype */ {
          internal_eContainer: {
            get: function () {
              return this.$internal_eContainer_itq4vg$;
            },
            set: function (tmp$0) {
              this.$internal_eContainer_itq4vg$ = tmp$0;
            }
          },
          internal_containmentRefName: {
            get: function () {
              return this.$internal_containmentRefName_ku82f8$;
            },
            set: function (tmp$0) {
              this.$internal_containmentRefName_ku82f8$ = tmp$0;
            }
          },
          internal_unsetCmd: {
            get: function () {
              return this.$internal_unsetCmd_goudjz$;
            },
            set: function (tmp$0) {
              this.$internal_unsetCmd_goudjz$ = tmp$0;
            }
          },
          internal_readOnlyElem: {
            get: function () {
              return this.$internal_readOnlyElem_t03kpt$;
            },
            set: function (tmp$0) {
              this.$internal_readOnlyElem_t03kpt$ = tmp$0;
            }
          },
          internal_recursive_readOnlyElem: {
            get: function () {
              return this.$internal_recursive_readOnlyElem_6vvxji$;
            },
            set: function (tmp$0) {
              this.$internal_recursive_readOnlyElem_6vvxji$ = tmp$0;
            }
          },
          internal_inboundReferences: {
            get: function () {
              return this.$internal_inboundReferences_piju7j$;
            },
            set: function (tmp$0) {
              this.$internal_inboundReferences_piju7j$ = tmp$0;
            }
          },
          internal_deleteInProgress: {
            get: function () {
              return this.$internal_deleteInProgress_juib5f$;
            },
            set: function (tmp$0) {
              this.$internal_deleteInProgress_juib5f$ = tmp$0;
            }
          },
          internal_is_deleted: {
            get: function () {
              return this.$internal_is_deleted_g0zd04$;
            },
            set: function (tmp$0) {
              this.$internal_is_deleted_g0zd04$ = tmp$0;
            }
          },
          is_root: {
            get: function () {
              return this.$is_root_ds72hn$;
            },
            set: function (tmp$0) {
              this.$is_root_ds72hn$ = tmp$0;
            }
          },
          path_cache: {
            get: function () {
              return this.$path_cache_ezsujq$;
            },
            set: function (tmp$0) {
              this.$path_cache_ezsujq$ = tmp$0;
            }
          },
          key_cache: {
            get: function () {
              return this.$key_cache_h93ze8$;
            },
            set: function (tmp$0) {
              this.$key_cache_h93ze8$ = tmp$0;
            }
          },
          delete: function () {
            this.internal_deleteInProgress = true;
            if (this.feature != null) {
              var tmp$0;
              ((tmp$0 = this.feature) != null ? tmp$0 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_feature);
              this.feature = null;
            }
            if (this.interpretation != null) {
              var tmp$1;
              ((tmp$1 = this.interpretation) != null ? tmp$1 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_interpretation);
              this.interpretation = null;
            }
            this.advertiseInboundRefs(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, this);
            this.internal_inboundReferences.clear();
            if (this.internal_unsetCmd != null) {
              var tmp$2;
              ((tmp$2 = this.internal_unsetCmd) != null ? tmp$2 : Kotlin.throwNPE()).run();
            }
            this.internal_is_deleted = true;
          },
          withContent: function (p) {
            this.content = p;
            return this;
          },
          content: {
            get: function () {
              return this.$content_whd7zt$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.content)) {
                var kmf_previousVal = this.$content_whd7zt$;
                this.$content_whd7zt$ = iP;
              }
            }
          },
          withGenerated_KMF_ID: function (p) {
            this.generated_KMF_ID = p;
            return this;
          },
          generated_KMF_ID: {
            get: function () {
              return this.$generated_KMF_ID_ln2zpk$;
            },
            set: function (iP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              if (!Kotlin.equals(iP, this.generated_KMF_ID)) {
                var oldId = this.internalGetKey();
                var previousParent = this.eContainer();
                var previousRefNameInParent = this.getRefInParent();
                this.path_cache = null;
                this.key_cache = null;
                var kmf_previousVal = this.$generated_KMF_ID_ln2zpk$;
                this.$generated_KMF_ID_ln2zpk$ = iP;
                if (previousParent != null) {
                  previousParent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX, previousRefNameInParent != null ? previousRefNameInParent : Kotlin.throwNPE(), oldId, false, false);
                }
              }
            }
          },
          feature: {
            get: function () {
              return this.$feature_6m8eys$;
            },
            set: function (featureP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              this.internal_feature(featureP, true, true);
            }
          },
          internal_feature: function (featureP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this.$feature_6m8eys$, featureP)) {
              var kmf_previousVal = this.$feature_6m8eys$;
              if (featureP != null) {
                (featureP != null ? featureP : Kotlin.throwNPE()).addInboundReference(this, _.pcm.util.Constants.Ref_feature);
              }
               else {
                if (this.$feature_6m8eys$ != null) {
                  var tmp$0;
                  ((tmp$0 = this.$feature_6m8eys$) != null ? tmp$0 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_feature);
                }
              }
              this.$feature_6m8eys$ = featureP;
            }
          },
          withFeature: function (ref) {
            return this;
          },
          interpretation: {
            get: function () {
              return this.$interpretation_ddutmu$;
            },
            set: function (interpretationP) {
              if (this.isReadOnly()) {
                throw new Error(_.pcm.util.Constants.READ_ONLY_EXCEPTION);
              }
              this.internal_interpretation(interpretationP, true, true);
            }
          },
          internal_interpretation: function (interpretationP, setOpposite, fireEvents) {
            if (!Kotlin.equals(this.$interpretation_ddutmu$, interpretationP)) {
              if (this.$interpretation_ddutmu$ != null) {
                var tmp$0;
                ((tmp$0 = this.$interpretation_ddutmu$) != null ? tmp$0 : Kotlin.throwNPE()).setEContainer(null, null, null);
              }
              if (interpretationP != null) {
                (interpretationP != null ? interpretationP : Kotlin.throwNPE()).setEContainer(this, new _.pcm.container.RemoveFromContainerCommand(this, _.org.kevoree.modeling.api.util.ActionType.object.SET, _.pcm.util.Constants.Ref_interpretation, null), _.pcm.util.Constants.Ref_interpretation);
              }
              var kmf_previousVal = this.$interpretation_ddutmu$;
              if (interpretationP != null) {
                (interpretationP != null ? interpretationP : Kotlin.throwNPE()).addInboundReference(this, _.pcm.util.Constants.Ref_interpretation);
              }
               else {
                if (this.$interpretation_ddutmu$ != null) {
                  var tmp$1;
                  ((tmp$1 = this.$interpretation_ddutmu$) != null ? tmp$1 : Kotlin.throwNPE()).removeInboundReference(this, _.pcm.util.Constants.Ref_interpretation);
                }
              }
              this.$interpretation_ddutmu$ = interpretationP;
            }
          },
          withInterpretation: function (ref) {
            return this;
          },
          reflexiveMutator: function (mutationType, refName, value, setOpposite, fireEvents) {
            {
              if (refName === _.pcm.util.Constants.Att_content) {
                this.content = value;
              }
               else if (refName === _.pcm.util.Constants.Att_generated_KMF_ID) {
                this.generated_KMF_ID = value;
              }
               else if (refName === _.pcm.util.Constants.Ref_feature) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.SET) {
                    this.feature = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.feature = null;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.feature = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else if (refName === _.pcm.util.Constants.Ref_interpretation) {
                {
                  if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.SET) {
                    this.interpretation = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                    this.interpretation = null;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                    this.interpretation = value;
                  }
                   else if (mutationType === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                  }
                   else {
                    throw new Error(_.pcm.util.Constants.UNKNOWN_MUTATION_TYPE_EXCEPTION + mutationType);
                  }
                }
              }
               else {
                throw new Error('Can not reflexively ' + mutationType + ' for ' + refName + ' on ' + this);
              }
            }
          },
          internalGetKey: function () {
            if (this.key_cache != null) {
              return this.key_cache;
            }
             else {
              this.key_cache = this.generated_KMF_ID;
            }
            return this.key_cache;
          },
          findByID: function (relationName, idP) {
            {
              if (relationName === _.pcm.util.Constants.Ref_feature) {
                var objFound = this.feature;
                if (objFound != null && Kotlin.equals(objFound.internalGetKey(), idP)) {
                  return objFound;
                }
                 else {
                  return null;
                }
              }
               else if (relationName === _.pcm.util.Constants.Ref_interpretation) {
                var objFound_0 = this.interpretation;
                if (objFound_0 != null && Kotlin.equals(objFound_0.internalGetKey(), idP)) {
                  return objFound_0;
                }
                 else {
                  return null;
                }
              }
               else {
                return null;
              }
            }
          },
          visit: function (visitor, recursive, containedReference, nonContainedReference) {
            visitor.beginVisitElem(this);
            if (containedReference) {
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_interpretation, _.pcm.util.Constants.pcm_Value)) {
                this.internal_visit(visitor, this.interpretation, recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_interpretation);
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_interpretation);
            }
            if (nonContainedReference) {
              if (visitor.beginVisitRef(_.pcm.util.Constants.Ref_feature, _.pcm.util.Constants.pcm_Feature)) {
                this.internal_visit(visitor, this.feature, recursive, containedReference, nonContainedReference, _.pcm.util.Constants.Ref_feature);
              }
              visitor.endVisitRef(_.pcm.util.Constants.Ref_feature);
            }
            visitor.endVisitElem(this);
          },
          visitAttributes: function (visitor) {
            visitor.visit(this.content, _.pcm.util.Constants.Att_content, this);
            visitor.visit(this.generated_KMF_ID, _.pcm.util.Constants.Att_generated_KMF_ID, this);
          },
          metaClassName: function () {
            return _.pcm.util.Constants.pcm_Cell;
          }
        })
      }),
      factory: Kotlin.definePackage(null, /** @lends _.pcm.factory */ {
        DefaultPcmFactory: Kotlin.createClass(function () {
          return [_.pcm.factory.PcmFactory];
        }, null, /** @lends _.pcm.factory.DefaultPcmFactory.prototype */ {
          getVersion: function () {
            return '1.0-SNAPSHOT';
          },
          lookup: function (path) {
            return null;
          },
          createPCM: function () {
            var tempElem = new _.pcm.impl.PCMImpl();
            return tempElem;
          },
          createProduct: function () {
            var tempElem = new _.pcm.impl.ProductImpl();
            return tempElem;
          },
          createAbstractFeature: function () {
            var tempElem = new _.pcm.impl.AbstractFeatureImpl();
            return tempElem;
          },
          createCell: function () {
            var tempElem = new _.pcm.impl.CellImpl();
            return tempElem;
          },
          createFeature: function () {
            var tempElem = new _.pcm.impl.FeatureImpl();
            return tempElem;
          },
          createFeatureGroup: function () {
            var tempElem = new _.pcm.impl.FeatureGroupImpl();
            return tempElem;
          },
          createValue: function () {
            var tempElem = new _.pcm.impl.ValueImpl();
            return tempElem;
          },
          createIntegerValue: function () {
            var tempElem = new _.pcm.impl.IntegerValueImpl();
            return tempElem;
          },
          createStringValue: function () {
            var tempElem = new _.pcm.impl.StringValueImpl();
            return tempElem;
          },
          createRealValue: function () {
            var tempElem = new _.pcm.impl.RealValueImpl();
            return tempElem;
          },
          createBooleanValue: function () {
            var tempElem = new _.pcm.impl.BooleanValueImpl();
            return tempElem;
          },
          createMultiple: function () {
            var tempElem = new _.pcm.impl.MultipleImpl();
            return tempElem;
          },
          createNotAvailable: function () {
            var tempElem = new _.pcm.impl.NotAvailableImpl();
            return tempElem;
          },
          createConditional: function () {
            var tempElem = new _.pcm.impl.ConditionalImpl();
            return tempElem;
          },
          createPartial: function () {
            var tempElem = new _.pcm.impl.PartialImpl();
            return tempElem;
          },
          createDateValue: function () {
            var tempElem = new _.pcm.impl.DateValueImpl();
            return tempElem;
          },
          createVersion: function () {
            var tempElem = new _.pcm.impl.VersionImpl();
            return tempElem;
          },
          createDimension: function () {
            var tempElem = new _.pcm.impl.DimensionImpl();
            return tempElem;
          },
          createNotApplicable: function () {
            var tempElem = new _.pcm.impl.NotApplicableImpl();
            return tempElem;
          },
          createUnit: function () {
            var tempElem = new _.pcm.impl.UnitImpl();
            return tempElem;
          },
          create: function (metaClassName) {
            {
              if (metaClassName === _.pcm.util.Constants.pcm_PCM) {
                return this.createPCM();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Product) {
                return this.createProduct();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_AbstractFeature) {
                return this.createAbstractFeature();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Cell) {
                return this.createCell();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Feature) {
                return this.createFeature();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_FeatureGroup) {
                return this.createFeatureGroup();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Value) {
                return this.createValue();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_IntegerValue) {
                return this.createIntegerValue();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_StringValue) {
                return this.createStringValue();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_RealValue) {
                return this.createRealValue();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_BooleanValue) {
                return this.createBooleanValue();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Multiple) {
                return this.createMultiple();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_NotAvailable) {
                return this.createNotAvailable();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Conditional) {
                return this.createConditional();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Partial) {
                return this.createPartial();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_DateValue) {
                return this.createDateValue();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Version) {
                return this.createVersion();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Dimension) {
                return this.createDimension();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_NotApplicable) {
                return this.createNotApplicable();
              }
               else if (metaClassName === _.pcm.util.Constants.pcm_Unit) {
                return this.createUnit();
              }
               else {
                return null;
              }
            }
          },
          select: function (query) {
            return new Kotlin.ArrayList();
          },
          root: function (elem) {
            elem.is_root = true;
            elem.path_cache = '/';
          },
          createJSONSerializer: function () {
            return new _.org.kevoree.modeling.api.json.JSONModelSerializer();
          },
          createJSONLoader: function () {
            return new _.org.kevoree.modeling.api.json.JSONModelLoader(this);
          },
          createXMISerializer: function () {
            return new _.org.kevoree.modeling.api.xmi.XMIModelSerializer();
          },
          createXMILoader: function () {
            return new _.org.kevoree.modeling.api.xmi.XMIModelLoader(this);
          },
          createModelCompare: function () {
            return new _.org.kevoree.modeling.api.compare.ModelCompare(this);
          },
          createModelCloner: function () {
            return new _.org.kevoree.modeling.api.ModelCloner(this);
          },
          createModelPruner: function () {
            return new _.org.kevoree.modeling.api.ModelPruner(this);
          }
        }),
        PcmFactory: Kotlin.createTrait(function () {
          return [_.org.kevoree.modeling.api.KMFFactory];
        })
      })
    }),
    java: Kotlin.definePackage(null, /** @lends _.java */ {
      io: Kotlin.definePackage(null, /** @lends _.java.io */ {
        InputStream: Kotlin.createTrait(null),
        OutputStream: Kotlin.createTrait(null),
        BufferedOutputStream: Kotlin.createClass(function () {
          return [_.java.io.OutputStream];
        }, function (oo) {
          this.oo = oo;
        }, /** @lends _.java.io.BufferedOutputStream.prototype */ {
          write: function (s) {
            this.oo.result = s;
          }
        }),
        ByteArrayInputStream: Kotlin.createClass(function () {
          return [_.java.io.InputStream];
        }, function (inputBytes) {
          this.inputBytes = inputBytes;
        }, /** @lends _.java.io.ByteArrayInputStream.prototype */ {
          readBytes: function () {
            return this.inputBytes;
          }
        }),
        ByteArrayOutputStream: Kotlin.createClass(function () {
          return [_.java.io.OutputStream];
        }, function () {
          this.result = '';
        }, /** @lends _.java.io.ByteArrayOutputStream.prototype */ {
          flush: function () {
          },
          close: function () {
          },
          toString: function () {
            return this.result;
          }
        }),
        PrintStream: Kotlin.createClass(null, function (oo, autoflush) {
          this.oo = oo;
          this.result = '';
        }, /** @lends _.java.io.PrintStream.prototype */ {
          println: function () {
            this.result = this.result + '\n';
          },
          print_4: function (s) {
            this.result = this.result + s;
          },
          println_2: function (s) {
            this.print_4(s);
            this.println();
          },
          print_1: function (s) {
            this.result = this.result + s;
          },
          print_2: function (s) {
            this.result = this.result + s;
          },
          print_3: function (s) {
            this.result = this.result + s;
          },
          print: function (s) {
            if (s) {
              this.result = this.result + 'true';
            }
             else {
              this.result = this.result + 'false';
            }
          },
          println_1: function (s) {
            this.print_1(s);
            this.println();
          },
          flush: function () {
            this.oo.write(this.result);
          },
          close: function () {
          }
        })
      }),
      util: Kotlin.definePackage(null, /** @lends _.java.util */ {
        concurrent: Kotlin.definePackage(null, /** @lends _.java.util.concurrent */ {
          ConcurrentHashMap: Kotlin.createClass(function () {
            return [Kotlin.HashMap];
          }, function $fun() {
            $fun.baseInitializer.call(this);
          })
        }),
        IdentityHashMap: Kotlin.createClass(function () {
          return [Kotlin.HashMap];
        }, function $fun() {
          $fun.baseInitializer.call(this);
        }),
        HashSet_xeylzf$: function (c) {
          var set = new Kotlin.ComplexHashSet(c.size());
          set.addAll_xeylzf$(c);
          return set;
        },
        LinkedHashSet_xeylzf$: function (c) {
          var set = new Kotlin.LinkedHashSet(c.size());
          set.addAll_xeylzf$(c);
          return set;
        },
        HashMap_za3j1t$: function (m) {
          var map = new Kotlin.ComplexHashMap(m.size());
          map.putAll_za3j1t$(m);
          return map;
        },
        LinkedHashMap_za3j1t$: function (m) {
          var map = new Kotlin.LinkedHashMap(m.size());
          map.putAll_za3j1t$(m);
          return map;
        }
      }),
      lang: Kotlin.definePackage(function () {
        this.Long = Kotlin.createObject(null, null, {
          parseLong: function (s) {
            return s;
          }
        });
        this.Integer = Kotlin.createObject(null, null, {
          parseInt: function (s) {
            return s;
          }
        });
      }, /** @lends _.java.lang */ {
      })
    }),
    js: Kotlin.definePackage(null, /** @lends _.js */ {
      toChar_mz3mef$: function ($receiver) {
        return $receiver;
      },
      lastIndexOf_orzsrp$: function ($receiver, ch, fromIndex) {
        return $receiver.lastIndexOf(ch.toString(), fromIndex);
      },
      lastIndexOf_960177$: function ($receiver, ch) {
        return $receiver.lastIndexOf(ch.toString());
      },
      indexOf_960177$: function ($receiver, ch) {
        return $receiver.indexOf(ch.toString());
      },
      indexOf_orzsrp$: function ($receiver, ch, fromIndex) {
        return $receiver.indexOf(ch.toString(), fromIndex);
      },
      matches_94jgcu$: function ($receiver, regex) {
        var result = $receiver.match(regex);
        return result != null && result.length > 0;
      },
      capitalize_pdl1w0$: function ($receiver) {
        return _.kotlin.isNotEmpty_pdl1w0$($receiver) ? $receiver.substring(0, 1).toUpperCase() + $receiver.substring(1) : $receiver;
      },
      decapitalize_pdl1w0$: function ($receiver) {
        return _.kotlin.isNotEmpty_pdl1w0$($receiver) ? $receiver.substring(0, 1).toLowerCase() + $receiver.substring(1) : $receiver;
      }
    }),
    org: Kotlin.definePackage(null, /** @lends _.org */ {
      kevoree: Kotlin.definePackage(null, /** @lends _.org.kevoree */ {
        modeling: Kotlin.definePackage(null, /** @lends _.org.kevoree.modeling */ {
          api: Kotlin.definePackage(null, /** @lends _.org.kevoree.modeling.api */ {
            compare: Kotlin.definePackage(null, /** @lends _.org.kevoree.modeling.api.compare */ {
              ModelCompare: Kotlin.createClass(null, function (factory) {
                this.factory = factory;
              }, /** @lends _.org.kevoree.modeling.api.compare.ModelCompare.prototype */ {
                diff: function (origin, target) {
                  return (new _.org.kevoree.modeling.api.trace.TraceSequence(this.factory)).populate(this.internal_diff(origin, target, false, false));
                },
                merge: function (origin, target) {
                  return (new _.org.kevoree.modeling.api.trace.TraceSequence(this.factory)).populate(this.internal_diff(origin, target, false, true));
                },
                inter: function (origin, target) {
                  return (new _.org.kevoree.modeling.api.trace.TraceSequence(this.factory)).populate(this.internal_diff(origin, target, true, false));
                },
                internal_diff: function (origin, target, inter, merge) {
                  var traces = new Kotlin.ArrayList();
                  var tracesRef = new Kotlin.ArrayList();
                  var objectsMap = new Kotlin.PrimitiveHashMap();
                  traces.addAll_xeylzf$(origin.createTraces(target, inter, merge, false, true));
                  tracesRef.addAll_xeylzf$(origin.createTraces(target, inter, merge, true, false));
                  var visitor = _.org.kevoree.modeling.api.compare.ModelCompare.internal_diff$f(objectsMap);
                  origin.visit(visitor, true, true, false);
                  var visitor2 = _.org.kevoree.modeling.api.compare.ModelCompare.internal_diff$f_0(objectsMap, inter, traces, merge, tracesRef);
                  target.visit(visitor2, true, true, false);
                  traces.addAll_xeylzf$(tracesRef);
                  if (!inter) {
                    if (!merge) {
                      {
                        var tmp$0 = objectsMap.keySet().iterator();
                        while (tmp$0.hasNext()) {
                          var diffChildKey = tmp$0.next();
                          var tmp$1, tmp$3, tmp$5;
                          var diffChild = (tmp$1 = objectsMap.get_za3rmp$(diffChildKey)) != null ? tmp$1 : Kotlin.throwNPE();
                          if (diffChild.eContainer() != null) {
                            var tmp$2;
                            tmp$3 = ((tmp$2 = diffChild.eContainer()) != null ? tmp$2 : Kotlin.throwNPE()).path();
                          }
                           else {
                            tmp$3 = 'null';
                          }
                          var src = tmp$3;
                          if (diffChild.getRefInParent() != null) {
                            var tmp$4;
                            tmp$5 = (tmp$4 = diffChild.getRefInParent()) != null ? tmp$4 : Kotlin.throwNPE();
                          }
                           else {
                            tmp$5 = 'null';
                          }
                          var refNameInParent = tmp$5;
                          traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelRemoveTrace(src, refNameInParent, diffChild.path()));
                        }
                      }
                    }
                  }
                  return traces;
                }
              }, /** @lends _.org.kevoree.modeling.api.compare.ModelCompare */ {
                internal_diff$f: function (objectsMap) {
                  return Kotlin.createObject(function () {
                    return [_.org.kevoree.modeling.api.util.ModelVisitor];
                  }, function $fun() {
                    $fun.baseInitializer.call(this);
                  }, {
                    visit: function (elem, refNameInParent, parent) {
                      var childPath = elem.path();
                      if (childPath != null) {
                        objectsMap.put_wn2jw4$(childPath, elem);
                      }
                       else {
                        throw new Error('Null child path ' + elem);
                      }
                    }
                  });
                },
                internal_diff$f_0: function (objectsMap, inter, traces, merge, tracesRef) {
                  return Kotlin.createObject(function () {
                    return [_.org.kevoree.modeling.api.util.ModelVisitor];
                  }, function $fun() {
                    $fun.baseInitializer.call(this);
                  }, {
                    visit: function (elem, refNameInParent, parent) {
                      var childPath = elem.path();
                      if (objectsMap.containsKey_za3rmp$(childPath)) {
                        if (inter) {
                          traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace(parent.path(), refNameInParent, elem.path(), elem.metaClassName()));
                        }
                        var tmp$0, tmp$1;
                        traces.addAll_xeylzf$(((tmp$0 = objectsMap.get_za3rmp$(childPath)) != null ? tmp$0 : Kotlin.throwNPE()).createTraces(elem, inter, merge, false, true));
                        tracesRef.addAll_xeylzf$(((tmp$1 = objectsMap.get_za3rmp$(childPath)) != null ? tmp$1 : Kotlin.throwNPE()).createTraces(elem, inter, merge, true, false));
                        objectsMap.remove_za3rmp$(childPath);
                      }
                       else {
                        if (!inter) {
                          traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace(parent.path(), refNameInParent, elem.path(), elem.metaClassName()));
                          traces.addAll_xeylzf$(elem.createTraces(elem, true, merge, false, true));
                          tracesRef.addAll_xeylzf$(elem.createTraces(elem, true, merge, true, false));
                        }
                      }
                    }
                  });
                }
              })
            }),
            ModelLoader: Kotlin.createTrait(null),
            KMFFactory: Kotlin.createTrait(null),
            KMFContainer: Kotlin.createTrait(null),
            Callback: Kotlin.createTrait(null),
            ModelSerializer: Kotlin.createTrait(null),
            ModelPruner: Kotlin.createClass(null, function (factory) {
              this.factory = factory;
            }, /** @lends _.org.kevoree.modeling.api.ModelPruner.prototype */ {
              prune: function (elems) {
                var traces = new Kotlin.ArrayList();
                var tempMap = new Kotlin.PrimitiveHashMap();
                var parentMap = new Kotlin.PrimitiveHashMap();
                {
                  var tmp$0 = elems.iterator();
                  while (tmp$0.hasNext()) {
                    var elem = tmp$0.next();
                    this.internal_prune(elem, traces, tempMap, parentMap);
                  }
                }
                {
                  var tmp$1 = tempMap.keySet().iterator();
                  while (tmp$1.hasNext()) {
                    var toLinkKey = tmp$1.next();
                    var tmp$2;
                    var toLink = (tmp$2 = tempMap.get_za3rmp$(toLinkKey)) != null ? tmp$2 : Kotlin.throwNPE();
                    traces.addAll_xeylzf$(toLink.toTraces(false, true));
                  }
                }
                return (new _.org.kevoree.modeling.api.trace.TraceSequence(this.factory)).populate(traces);
              },
              internal_prune: function (elem, traces, cache, parentMap) {
                var parents = new Kotlin.ArrayList();
                var currentParent = elem.eContainer();
                while (currentParent != null && parentMap.get_za3rmp$((currentParent != null ? currentParent : Kotlin.throwNPE()).path()) == null && cache.get_za3rmp$((currentParent != null ? currentParent : Kotlin.throwNPE()).path()) == null) {
                  parents.add_za3rmp$(currentParent != null ? currentParent : Kotlin.throwNPE());
                  currentParent = (currentParent != null ? currentParent : Kotlin.throwNPE()).eContainer();
                }
                {
                  var tmp$0 = _.kotlin.reverse_h3panj$(parents).iterator();
                  while (tmp$0.hasNext()) {
                    var parent = tmp$0.next();
                    if (parent.eContainer() != null) {
                      var tmp$1, tmp$2;
                      traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace(((tmp$1 = parent.eContainer()) != null ? tmp$1 : Kotlin.throwNPE()).path(), (tmp$2 = parent.getRefInParent()) != null ? tmp$2 : Kotlin.throwNPE(), parent.path(), parent.metaClassName()));
                    }
                    traces.addAll_xeylzf$(parent.toTraces(true, false));
                    parentMap.put_wn2jw4$(parent.path(), parent);
                  }
                }
                if (cache.get_za3rmp$(elem.path()) == null && parentMap.get_za3rmp$(elem.path()) == null) {
                  if (elem.eContainer() != null) {
                    var tmp$3, tmp$4;
                    traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace(((tmp$3 = elem.eContainer()) != null ? tmp$3 : Kotlin.throwNPE()).path(), (tmp$4 = elem.getRefInParent()) != null ? tmp$4 : Kotlin.throwNPE(), elem.path(), elem.metaClassName()));
                  }
                  traces.addAll_xeylzf$(elem.toTraces(true, false));
                }
                cache.put_wn2jw4$(elem.path(), elem);
                elem.visitReferences(_.org.kevoree.modeling.api.ModelPruner.internal_prune$f(cache, this, traces, parentMap));
              }
            }, /** @lends _.org.kevoree.modeling.api.ModelPruner */ {
              internal_prune$f: function (cache, this$ModelPruner, traces, parentMap) {
                return Kotlin.createObject(function () {
                  return [_.org.kevoree.modeling.api.util.ModelVisitor];
                }, function $fun() {
                  $fun.baseInitializer.call(this);
                }, {
                  visit: function (elem, refNameInParent, parent) {
                    if (cache.get_za3rmp$(elem.path()) == null) {
                      this$ModelPruner.internal_prune(elem, traces, cache, parentMap);
                    }
                  }
                });
              }
            }),
            ModelCloner: Kotlin.createClass(null, function (factory) {
              this.factory = factory;
            }, /** @lends _.org.kevoree.modeling.api.ModelCloner.prototype */ {
              createContext: function () {
                return new _.java.util.IdentityHashMap();
              },
              clone: function (o) {
                return this.clone_1(o, false);
              },
              clone_1: function (o, readOnly) {
                return this.clone_2(o, readOnly, false);
              },
              cloneMutableOnly: function (o, readOnly) {
                return this.clone_2(o, readOnly, true);
              },
              cloneModelElem: function (src) {
                var tmp$0;
                var clonedSrc = (tmp$0 = this.factory.create(src.metaClassName())) != null ? tmp$0 : Kotlin.throwNPE();
                var attributesCloner = _.org.kevoree.modeling.api.ModelCloner.cloneModelElem$f(clonedSrc);
                src.visitAttributes(attributesCloner);
                return clonedSrc;
              },
              resolveModelElem: function (src, target, context, mutableOnly) {
                var refResolver = _.org.kevoree.modeling.api.ModelCloner.resolveModelElem$f(mutableOnly, target, context);
                src.visit(refResolver, false, true, true);
              },
              clone_2: function (o, readOnly, mutableOnly) {
                var context = this.createContext();
                var clonedObject = this.cloneModelElem(o);
                context.put_wn2jw4$(o, clonedObject);
                var cloneGraphVisitor = _.org.kevoree.modeling.api.ModelCloner.clone_2$f(mutableOnly, context, this);
                o.visit(cloneGraphVisitor, true, true, false);
                var resolveGraphVisitor = _.org.kevoree.modeling.api.ModelCloner.clone_2$f_0(mutableOnly, context, this, readOnly);
                this.resolveModelElem(o, clonedObject, context, mutableOnly);
                o.visit(resolveGraphVisitor, true, true, false);
                if (readOnly) {
                  clonedObject.setInternalReadOnly();
                }
                if (o.isRoot()) {
                  this.factory.root(clonedObject);
                }
                return clonedObject;
              }
            }, /** @lends _.org.kevoree.modeling.api.ModelCloner */ {
              cloneModelElem$f: function (clonedSrc) {
                return Kotlin.createObject(function () {
                  return [_.org.kevoree.modeling.api.util.ModelAttributeVisitor];
                }, null, {
                  visit: function (value, name, parent) {
                    if (value != null) {
                      if (Kotlin.isType(value, Kotlin.ArrayList)) {
                        var clonedList = new Kotlin.ArrayList();
                        clonedList.addAll_xeylzf$(value != null ? value : Kotlin.throwNPE());
                        clonedSrc.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.SET, name, clonedList, false, false);
                      }
                       else {
                        clonedSrc.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.SET, name, value, false, false);
                      }
                    }
                  }
                });
              },
              resolveModelElem$f: function (mutableOnly, target, context) {
                return Kotlin.createObject(function () {
                  return [_.org.kevoree.modeling.api.util.ModelVisitor];
                }, function $fun() {
                  $fun.baseInitializer.call(this);
                }, {
                  visit: function (elem, refNameInParent, parent) {
                    if (mutableOnly && elem.isRecursiveReadOnly()) {
                      target.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, refNameInParent, elem, false, false);
                    }
                     else {
                      var elemResolved = context.get_za3rmp$(elem);
                      if (elemResolved == null) {
                        throw new Error('Cloner error, not self-contain model, the element ' + elem.path() + ' is contained in the root element');
                      }
                      target.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, refNameInParent, elemResolved, false, false);
                    }
                  }
                });
              },
              clone_2$f: function (mutableOnly, context, this$ModelCloner) {
                return Kotlin.createObject(function () {
                  return [_.org.kevoree.modeling.api.util.ModelVisitor];
                }, function $fun() {
                  $fun.baseInitializer.call(this);
                }, {
                  visit: function (elem, refNameInParent, parent) {
                    if (mutableOnly && elem.isRecursiveReadOnly()) {
                      this.noChildrenVisit();
                    }
                     else {
                      context.put_wn2jw4$(elem, this$ModelCloner.cloneModelElem(elem));
                    }
                  }
                });
              },
              clone_2$f_0: function (mutableOnly, context, this$ModelCloner, readOnly) {
                return Kotlin.createObject(function () {
                  return [_.org.kevoree.modeling.api.util.ModelVisitor];
                }, function $fun() {
                  $fun.baseInitializer.call(this);
                }, {
                  visit: function (elem, refNameInParent, parent) {
                    if (mutableOnly && elem.isRecursiveReadOnly()) {
                    }
                     else {
                      var tmp$0;
                      var clonedObj = (tmp$0 = context.get_za3rmp$(elem)) != null ? tmp$0 : Kotlin.throwNPE();
                      this$ModelCloner.resolveModelElem(elem, clonedObj, context, mutableOnly);
                      if (readOnly) {
                        clonedObj.setInternalReadOnly();
                      }
                    }
                  }
                });
              }
            }),
            TimedContainer: Kotlin.createTrait(function () {
              return [_.org.kevoree.modeling.api.KMFContainer];
            }, /** @lends _.org.kevoree.modeling.api.TimedContainer.prototype */ {
              now: {
                get: function () {
                  return this.$now_2vpa82$;
                },
                set: function (tmp$0) {
                  this.$now_2vpa82$ = tmp$0;
                }
              }
            }),
            TransactionManager: Kotlin.createTrait(null),
            Transaction: Kotlin.createTrait(null),
            TimeTransaction: Kotlin.createTrait(function () {
              return [_.org.kevoree.modeling.api.Transaction];
            }),
            util: Kotlin.definePackage(function () {
              this.Selector = Kotlin.createObject(null, null, {
                select: function (root, query) {
                  var extractedQuery = this.extractFirstQuery(query);
                  var result = new Kotlin.ArrayList();
                  var tempResult = {v: new Kotlin.PrimitiveHashMap()};
                  tempResult.v.put_wn2jw4$(root.path(), root);
                  while (extractedQuery != null) {
                    var staticExtractedQuery = extractedQuery != null ? extractedQuery : Kotlin.throwNPE();
                    var clonedRound = tempResult.v;
                    tempResult.v = new Kotlin.PrimitiveHashMap();
                    {
                      var tmp$0 = clonedRound.keySet().iterator();
                      while (tmp$0.hasNext()) {
                        var currentRootKey = tmp$0.next();
                        var tmp$1;
                        var currentRoot = (tmp$1 = clonedRound.get_za3rmp$(currentRootKey)) != null ? tmp$1 : Kotlin.throwNPE();
                        var resolved = null;
                        if (!staticExtractedQuery.oldString.contains('*')) {
                          resolved = currentRoot.findByPath(staticExtractedQuery.oldString);
                        }
                        if (resolved != null) {
                          tempResult.v.put_wn2jw4$((resolved != null ? resolved : Kotlin.throwNPE()).path(), resolved != null ? resolved : Kotlin.throwNPE());
                        }
                         else {
                          var alreadyVisited = {v: new Kotlin.PrimitiveHashMap()};
                          var visitor = _.org.kevoree.modeling.api.util.select$f(staticExtractedQuery, alreadyVisited, tempResult);
                          if (staticExtractedQuery.previousIsDeep) {
                            currentRoot.visit(visitor, false, true, staticExtractedQuery.previousIsRefDeep);
                          }
                           else {
                            currentRoot.visit(visitor, false, true, true);
                          }
                        }
                      }
                    }
                    if (staticExtractedQuery.subQuery == null) {
                      extractedQuery = null;
                    }
                     else {
                      extractedQuery = this.extractFirstQuery(staticExtractedQuery.subQuery);
                    }
                  }
                  {
                    var tmp$2 = tempResult.v.keySet().iterator();
                    while (tmp$2.hasNext()) {
                      var v = tmp$2.next();
                      var tmp$3;
                      result.add_za3rmp$((tmp$3 = tempResult.v.get_za3rmp$(v)) != null ? tmp$3 : Kotlin.throwNPE());
                    }
                  }
                  return result;
                },
                extractFirstQuery: function (query) {
                  if (query.charAt(0) === '/') {
                    var subQuery = null;
                    if (query.length > 1) {
                      subQuery = query.substring(1);
                    }
                    var params = new Kotlin.PrimitiveHashMap();
                    return new _.org.kevoree.modeling.api.util.KmfQuery('', params, subQuery, '/', false, false);
                  }
                  if (query.startsWith('**/')) {
                    if (query.length > 3) {
                      var next = this.extractFirstQuery(query.substring(3));
                      if (next != null) {
                        next.previousIsDeep = true;
                        next.previousIsRefDeep = false;
                      }
                      return next;
                    }
                     else {
                      return null;
                    }
                  }
                  if (query.startsWith('***/')) {
                    if (query.length > 4) {
                      var next_0 = this.extractFirstQuery(query.substring(4));
                      if (next_0 != null) {
                        next_0.previousIsDeep = true;
                        next_0.previousIsRefDeep = true;
                      }
                      return next_0;
                    }
                     else {
                      return null;
                    }
                  }
                  var i = 0;
                  var relationNameEnd = 0;
                  var attsEnd = 0;
                  var escaped = false;
                  while (i < query.length && (query.charAt(i) !== '/' || escaped)) {
                    if (escaped) {
                      escaped = false;
                    }
                    if (query.charAt(i) === '[') {
                      relationNameEnd = i;
                    }
                     else {
                      if (query.charAt(i) === ']') {
                        attsEnd = i;
                      }
                       else {
                        if (query.charAt(i) === '\\') {
                          escaped = true;
                        }
                      }
                    }
                    i = i + 1;
                  }
                  if (i > 0 && relationNameEnd > 0) {
                    var oldString = query.substring(0, i);
                    var subQuery_0 = null;
                    if (i + 1 < query.length) {
                      subQuery_0 = query.substring(i + 1);
                    }
                    var relName = query.substring(0, relationNameEnd);
                    var params_0 = new Kotlin.PrimitiveHashMap();
                    relName = relName.replace('\\', '');
                    if (attsEnd !== 0) {
                      var paramString = query.substring(relationNameEnd + 1, attsEnd);
                      var iParam = 0;
                      var lastStart = iParam;
                      escaped = false;
                      while (iParam < paramString.length) {
                        if (paramString.charAt(iParam) === ',' && !escaped) {
                          var p = paramString.substring(lastStart, iParam).trim();
                          if (!Kotlin.equals(p, '') && !Kotlin.equals(p, '*')) {
                            if (p.endsWith('=')) {
                              p = p + '*';
                            }
                            var pArray = Kotlin.splitString(p, '=');
                            var pObject;
                            if (pArray.length > 1) {
                              var paramKey = pArray[0].trim();
                              var negative = paramKey.endsWith('!');
                              pObject = new _.org.kevoree.modeling.api.util.KmfQueryParam(paramKey.replace('!', ''), pArray[1].trim(), _.kotlin.get_size(params_0), negative);
                              var tmp$0;
                              params_0.put_wn2jw4$((tmp$0 = pObject.name) != null ? tmp$0 : Kotlin.throwNPE(), pObject);
                            }
                             else {
                              pObject = new _.org.kevoree.modeling.api.util.KmfQueryParam(null, p, _.kotlin.get_size(params_0), false);
                              params_0.put_wn2jw4$('@id', pObject);
                            }
                          }
                          lastStart = iParam + 1;
                        }
                         else {
                          if (paramString.charAt(iParam) === '\\') {
                            escaped = true;
                          }
                           else {
                            escaped = false;
                          }
                        }
                        iParam = iParam + 1;
                      }
                      var lastParam = paramString.substring(lastStart, iParam).trim();
                      if (!Kotlin.equals(lastParam, '') && !Kotlin.equals(lastParam, '*')) {
                        if (lastParam.endsWith('=')) {
                          lastParam = lastParam + '*';
                        }
                        var pArray_0 = Kotlin.splitString(lastParam, '=');
                        var pObject_0;
                        if (pArray_0.length > 1) {
                          var paramKey_0 = pArray_0[0].trim();
                          var negative_0 = paramKey_0.endsWith('!');
                          pObject_0 = new _.org.kevoree.modeling.api.util.KmfQueryParam(paramKey_0.replace('!', ''), pArray_0[1].trim(), _.kotlin.get_size(params_0), negative_0);
                          var tmp$1;
                          params_0.put_wn2jw4$((tmp$1 = pObject_0.name) != null ? tmp$1 : Kotlin.throwNPE(), pObject_0);
                        }
                         else {
                          pObject_0 = new _.org.kevoree.modeling.api.util.KmfQueryParam(null, lastParam, _.kotlin.get_size(params_0), false);
                          params_0.put_wn2jw4$('@id', pObject_0);
                        }
                      }
                    }
                    return new _.org.kevoree.modeling.api.util.KmfQuery(relName, params_0, subQuery_0, oldString, false, false);
                  }
                  return null;
                }
              });
              this.ByteConverter = Kotlin.createObject(null, null, {
                toChar: function (b) {
                  return b;
                },
                fromChar: function (b) {
                  return b;
                },
                byteArrayInputStreamFromString: function (str) {
                  var bytes = Kotlin.numberArrayOfSize(str.length);
                  var i = 0;
                  while (i < str.length) {
                    bytes[i] = str.charAt(i);
                    i = i + 1;
                  }
                  return new _.java.io.ByteArrayInputStream(bytes);
                }
              });
              this.AttConverter = Kotlin.createObject(null, null, {
                convFlatAtt: function (value) {
                  if (value == null) {
                    return null;
                  }
                  if (Kotlin.isType(value, Kotlin.ArrayList)) {
                    var isF = true;
                    var buffer = new Kotlin.StringBuilder();
                    {
                      var tmp$0 = value.iterator();
                      while (tmp$0.hasNext()) {
                        var v = tmp$0.next();
                        if (!isF) {
                          buffer.append('$');
                        }
                        buffer.append(Kotlin.toString(v));
                        isF = false;
                      }
                    }
                    return buffer.toString();
                  }
                   else {
                    return value.toString();
                  }
                },
                convAttFlat: function (value) {
                  return Kotlin.splitString(value.toString(), '$');
                }
              });
              this.KevURLEncoder = Kotlin.createObject(null, function () {
                this.nonEscaped_rysd1l$ = new Kotlin.PrimitiveHashMap();
                this.escaped_qojrqa$ = new Kotlin.PrimitiveHashMap();
                this.rescaped_9ikhle$ = new Kotlin.PrimitiveHashMap();
                var i = 'a';
                while (i < 'z') {
                  this.nonEscaped_rysd1l$.put_wn2jw4$(i, true);
                  i++;
                }
                i = 'A';
                while (i < 'Z') {
                  this.nonEscaped_rysd1l$.put_wn2jw4$(i, true);
                  i++;
                }
                i = '0';
                while (i < '9') {
                  this.nonEscaped_rysd1l$.put_wn2jw4$(i, true);
                  i++;
                }
                this.escaped_qojrqa$.put_wn2jw4$('!', '%21');
                this.escaped_qojrqa$.put_wn2jw4$('"', '%22');
                this.escaped_qojrqa$.put_wn2jw4$('#', '%23');
                this.escaped_qojrqa$.put_wn2jw4$('$', '%24');
                this.escaped_qojrqa$.put_wn2jw4$('%', '%25');
                this.escaped_qojrqa$.put_wn2jw4$('&', '%26');
                this.escaped_qojrqa$.put_wn2jw4$('*', '%2A');
                this.escaped_qojrqa$.put_wn2jw4$(',', '%2C');
                this.escaped_qojrqa$.put_wn2jw4$('/', '%2F');
                this.escaped_qojrqa$.put_wn2jw4$(']', '%5B');
                this.escaped_qojrqa$.put_wn2jw4$('\\', '%5c');
                this.escaped_qojrqa$.put_wn2jw4$('[', '%5D');
                {
                  var tmp$0 = this.escaped_qojrqa$.keySet().iterator();
                  while (tmp$0.hasNext()) {
                    var c = tmp$0.next();
                    var tmp$1;
                    this.rescaped_9ikhle$.put_wn2jw4$((tmp$1 = this.escaped_qojrqa$.get_za3rmp$(c)) != null ? tmp$1 : Kotlin.throwNPE(), c);
                  }
                }
              }, {
                encode: function (chain) {
                  if (chain == null) {
                    return null;
                  }
                  var buffer = null;
                  var i = 0;
                  while (i < chain.length) {
                    var ch = chain.charAt(i);
                    if (_.kotlin.contains_6halgi$(this.nonEscaped_rysd1l$, ch)) {
                      if (buffer != null) {
                        (buffer != null ? buffer : Kotlin.throwNPE()).append(ch);
                      }
                    }
                     else {
                      var resolved = this.escaped_qojrqa$.get_za3rmp$(ch);
                      if (resolved != null) {
                        if (buffer == null) {
                          buffer = new Kotlin.StringBuilder();
                          (buffer != null ? buffer : Kotlin.throwNPE()).append(chain.substring(0, i));
                        }
                        (buffer != null ? buffer : Kotlin.throwNPE()).append(resolved);
                      }
                    }
                    i = i + 1;
                  }
                  if (buffer != null) {
                    return Kotlin.toString(buffer);
                  }
                   else {
                    return chain;
                  }
                },
                decode: function (src) {
                  if (src == null) {
                    return null;
                  }
                  if (src.length === 0) {
                    return src;
                  }
                  var builder = null;
                  var i = 0;
                  while (i < src.length) {
                    var current = src.charAt(i);
                    if (current === '%') {
                      if (builder == null) {
                        builder = new Kotlin.StringBuilder();
                        builder != null ? builder.append(src.substring(0, i)) : null;
                      }
                      var key = current.toString() + src.charAt(i + 1) + src.charAt(i + 2);
                      var resolved = this.rescaped_9ikhle$.get_za3rmp$(key);
                      if (resolved == null) {
                        builder = builder != null ? builder.append(key) : null;
                      }
                       else {
                        builder = builder != null ? builder.append(resolved) : null;
                      }
                      i = i + 2;
                    }
                     else {
                      if (builder != null) {
                        builder = builder != null ? builder.append(current) : null;
                      }
                    }
                    i++;
                  }
                  if (builder != null) {
                    return (builder != null ? builder : Kotlin.throwNPE()).toString();
                  }
                   else {
                    return src;
                  }
                }
              });
            }, /** @lends _.org.kevoree.modeling.api.util */ {
              visit$f: function (i) {
                return false;
              },
              visit$f_0: function (staticExtractedQuery, subResult) {
                return Kotlin.createObject(function () {
                  return [_.org.kevoree.modeling.api.util.ModelAttributeVisitor];
                }, null, {
                  visit: function (value, name, parent) {
                    {
                      var tmp$0 = staticExtractedQuery.params.keySet().iterator();
                      while (tmp$0.hasNext()) {
                        var att = tmp$0.next();
                        if (Kotlin.equals(att, '@id')) {
                          throw new Error('Malformed KMFQuery, bad selector attribute without attribute name : ' + staticExtractedQuery.params.get_za3rmp$(att));
                        }
                         else {
                          var keySelected = false;
                          if (Kotlin.equals(att, name)) {
                            keySelected = true;
                          }
                           else {
                            if (att.contains('*') && _.js.matches_94jgcu$(name, att.replace('*', '.*'))) {
                              keySelected = true;
                            }
                          }
                          var tmp$1;
                          var attvalue = (tmp$1 = staticExtractedQuery.params.get_za3rmp$(att)) != null ? tmp$1 : Kotlin.throwNPE();
                          if (keySelected) {
                            if (value == null) {
                              if (attvalue.negative) {
                                if (!Kotlin.equals(attvalue.value, 'null')) {
                                  subResult[attvalue.idParam] = true;
                                }
                              }
                               else {
                                if (Kotlin.equals(attvalue.value, 'null')) {
                                  subResult[attvalue.idParam] = true;
                                }
                              }
                            }
                             else {
                              if (attvalue.negative) {
                                if (!attvalue.value.contains('*') && !Kotlin.equals(value, attvalue.value)) {
                                  subResult[attvalue.idParam] = true;
                                }
                                 else {
                                  if (!_.js.matches_94jgcu$(value.toString(), attvalue.value.replace('*', '.*'))) {
                                    subResult[attvalue.idParam] = true;
                                  }
                                }
                              }
                               else {
                                if (Kotlin.equals(value, attvalue.value)) {
                                  subResult[attvalue.idParam] = true;
                                }
                                 else {
                                  if (_.js.matches_94jgcu$(value.toString(), attvalue.value.replace('*', '.*'))) {
                                    subResult[attvalue.idParam] = true;
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                });
              },
              select$f: function (staticExtractedQuery, alreadyVisited, tempResult) {
                return Kotlin.createObject(function () {
                  return [_.org.kevoree.modeling.api.util.ModelVisitor];
                }, function $fun() {
                  $fun.baseInitializer.call(this);
                }, {
                  beginVisitRef: function (refName, refType) {
                    if (staticExtractedQuery.previousIsDeep) {
                      return true;
                    }
                     else {
                      if (Kotlin.equals(refName, staticExtractedQuery.relationName)) {
                        return true;
                      }
                       else {
                        if (staticExtractedQuery.relationName.contains('*')) {
                          if (_.js.matches_94jgcu$(refName, staticExtractedQuery.relationName.replace('*', '.*'))) {
                            return true;
                          }
                        }
                      }
                      return false;
                    }
                  },
                  visit: function (elem, refNameInParent, parent) {
                    if (staticExtractedQuery.previousIsRefDeep) {
                      if (alreadyVisited.v.containsKey_za3rmp$(parent.path() + '/' + refNameInParent + '[' + elem.internalGetKey() + ']')) {
                        return;
                      }
                    }
                    if (staticExtractedQuery.previousIsDeep && !staticExtractedQuery.previousIsRefDeep) {
                      if (alreadyVisited.v.containsKey_za3rmp$(elem.path())) {
                        return;
                      }
                    }
                    var selected = true;
                    if (staticExtractedQuery.previousIsDeep) {
                      selected = false;
                      if (Kotlin.equals(refNameInParent, staticExtractedQuery.relationName)) {
                        selected = true;
                      }
                       else {
                        if (staticExtractedQuery.relationName.contains('*')) {
                          if (_.js.matches_94jgcu$(refNameInParent, staticExtractedQuery.relationName.replace('*', '.*'))) {
                            selected = true;
                          }
                        }
                      }
                    }
                    if (selected) {
                      var tmp$0;
                      if (_.kotlin.get_size(staticExtractedQuery.params) === 1 && staticExtractedQuery.params.get_za3rmp$('@id') != null && ((tmp$0 = staticExtractedQuery.params.get_za3rmp$('@id')) != null ? tmp$0 : Kotlin.throwNPE()).name == null) {
                        var tmp$1;
                        if (Kotlin.equals(elem.internalGetKey(), (tmp$1 = staticExtractedQuery.params.get_za3rmp$('@id')) != null ? tmp$1.value : null)) {
                          tempResult.v.put_wn2jw4$(elem.path(), elem);
                        }
                      }
                       else {
                        if (_.kotlin.get_size(staticExtractedQuery.params) > 0) {
                          var subResult = Kotlin.arrayFromFun(_.kotlin.get_size(staticExtractedQuery.params), _.org.kevoree.modeling.api.util.visit$f);
                          elem.visitAttributes(_.org.kevoree.modeling.api.util.visit$f_0(staticExtractedQuery, subResult));
                          var finalRes = true;
                          var tmp$2, tmp$3, tmp$4;
                          {
                            tmp$2 = subResult, tmp$3 = tmp$2.length;
                            for (var tmp$4 = 0; tmp$4 !== tmp$3; ++tmp$4) {
                              var sub = tmp$2[tmp$4];
                              if (!sub) {
                                finalRes = false;
                              }
                            }
                          }
                          if (finalRes) {
                            tempResult.v.put_wn2jw4$(elem.path(), elem);
                          }
                        }
                         else {
                          tempResult.v.put_wn2jw4$(elem.path(), elem);
                        }
                      }
                    }
                    if (staticExtractedQuery.previousIsDeep) {
                      if (staticExtractedQuery.previousIsRefDeep) {
                        alreadyVisited.v.put_wn2jw4$(parent.path() + '/' + refNameInParent + '[' + elem.internalGetKey() + ']', true);
                        elem.visit(this, false, true, true);
                      }
                       else {
                        alreadyVisited.v.put_wn2jw4$(elem.path(), true);
                        elem.visit(this, false, true, false);
                      }
                    }
                  }
                });
              },
              KmfQuery: Kotlin.createClass(null, function (relationName, params, subQuery, oldString, previousIsDeep, previousIsRefDeep) {
                this.relationName = relationName;
                this.params = params;
                this.subQuery = subQuery;
                this.oldString = oldString;
                this.previousIsDeep = previousIsDeep;
                this.previousIsRefDeep = previousIsRefDeep;
              }, /** @lends _.org.kevoree.modeling.api.util.KmfQuery.prototype */ {
                component1: function () {
                  return this.relationName;
                },
                component2: function () {
                  return this.params;
                },
                component3: function () {
                  return this.subQuery;
                },
                component4: function () {
                  return this.oldString;
                },
                component5: function () {
                  return this.previousIsDeep;
                },
                component6: function () {
                  return this.previousIsRefDeep;
                },
                copy: function (relationName, params, subQuery, oldString, previousIsDeep, previousIsRefDeep) {
                  return new _.org.kevoree.modeling.api.util.KmfQuery(relationName === void 0 ? this.relationName : relationName, params === void 0 ? this.params : params, subQuery === void 0 ? this.subQuery : subQuery, oldString === void 0 ? this.oldString : oldString, previousIsDeep === void 0 ? this.previousIsDeep : previousIsDeep, previousIsRefDeep === void 0 ? this.previousIsRefDeep : previousIsRefDeep);
                },
                toString: function () {
                  return 'KmfQuery(relationName=' + Kotlin.toString(this.relationName) + (', params=' + Kotlin.toString(this.params)) + (', subQuery=' + Kotlin.toString(this.subQuery)) + (', oldString=' + Kotlin.toString(this.oldString)) + (', previousIsDeep=' + Kotlin.toString(this.previousIsDeep)) + (', previousIsRefDeep=' + Kotlin.toString(this.previousIsRefDeep)) + ')';
                },
                hashCode: function () {
                  var result = -1987101201;
                  result = result * 31 + Kotlin.hashCode(this.relationName) | 0;
                  result = result * 31 + Kotlin.hashCode(this.params) | 0;
                  result = result * 31 + Kotlin.hashCode(this.subQuery) | 0;
                  result = result * 31 + Kotlin.hashCode(this.oldString) | 0;
                  result = result * 31 + Kotlin.hashCode(this.previousIsDeep) | 0;
                  result = result * 31 + Kotlin.hashCode(this.previousIsRefDeep) | 0;
                  return result;
                },
                equals_za3rmp$: function (other) {
                  return this === other || (other !== null && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.relationName, other.relationName) && Kotlin.equals(this.params, other.params) && Kotlin.equals(this.subQuery, other.subQuery) && Kotlin.equals(this.oldString, other.oldString) && Kotlin.equals(this.previousIsDeep, other.previousIsDeep) && Kotlin.equals(this.previousIsRefDeep, other.previousIsRefDeep))));
                }
              }),
              KmfQueryParam: Kotlin.createClass(null, function (name, value, idParam, negative) {
                this.name = name;
                this.value = value;
                this.idParam = idParam;
                this.negative = negative;
              }, /** @lends _.org.kevoree.modeling.api.util.KmfQueryParam.prototype */ {
                component1: function () {
                  return this.name;
                },
                component2: function () {
                  return this.value;
                },
                component3: function () {
                  return this.idParam;
                },
                component4: function () {
                  return this.negative;
                },
                copy: function (name, value, idParam, negative) {
                  return new _.org.kevoree.modeling.api.util.KmfQueryParam(name === void 0 ? this.name : name, value === void 0 ? this.value : value, idParam === void 0 ? this.idParam : idParam, negative === void 0 ? this.negative : negative);
                },
                toString: function () {
                  return 'KmfQueryParam(name=' + Kotlin.toString(this.name) + (', value=' + Kotlin.toString(this.value)) + (', idParam=' + Kotlin.toString(this.idParam)) + (', negative=' + Kotlin.toString(this.negative)) + ')';
                },
                hashCode: function () {
                  var result = -874887202;
                  result = result * 31 + Kotlin.hashCode(this.name) | 0;
                  result = result * 31 + Kotlin.hashCode(this.value) | 0;
                  result = result * 31 + Kotlin.hashCode(this.idParam) | 0;
                  result = result * 31 + Kotlin.hashCode(this.negative) | 0;
                  return result;
                },
                equals_za3rmp$: function (other) {
                  return this === other || (other !== null && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.name, other.name) && Kotlin.equals(this.value, other.value) && Kotlin.equals(this.idParam, other.idParam) && Kotlin.equals(this.negative, other.negative))));
                }
              }),
              ElementAttributeType: Kotlin.createClass(function () {
                return [Kotlin.Enum];
              }, function $fun() {
                $fun.baseInitializer.call(this);
              }, null, /** @lends _.org.kevoree.modeling.api.util.ElementAttributeType */ {
                object_initializer$: function () {
                  return Kotlin.createEnumEntries({
                    ATTRIBUTE: new _.org.kevoree.modeling.api.util.ElementAttributeType(),
                    REFERENCE: new _.org.kevoree.modeling.api.util.ElementAttributeType(),
                    CONTAINMENT: new _.org.kevoree.modeling.api.util.ElementAttributeType()
                  });
                }
              }),
              InboundRefAware: Kotlin.createTrait(null, /** @lends _.org.kevoree.modeling.api.util.InboundRefAware.prototype */ {
                internal_inboundReferences: {
                  get: function () {
                    return this.$internal_inboundReferences_geftyz$;
                  },
                  set: function (tmp$0) {
                    this.$internal_inboundReferences_geftyz$ = tmp$0;
                  }
                }
              }),
              ModelVisitor: Kotlin.createClass(null, function () {
                this.visitStopped = false;
                this.visitChildren = true;
                this.visitReferences = true;
                this.alreadyVisited = null;
              }, /** @lends _.org.kevoree.modeling.api.util.ModelVisitor.prototype */ {
                stopVisit: function () {
                  this.visitStopped = true;
                },
                noChildrenVisit: function () {
                  this.visitChildren = false;
                },
                noReferencesVisit: function () {
                  this.visitReferences = false;
                },
                beginVisitElem: function (elem) {
                },
                endVisitElem: function (elem) {
                },
                beginVisitRef: function (refName, refType) {
                  return true;
                },
                endVisitRef: function (refName) {
                }
              }),
              ModelTracker: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.events.ModelElementListener];
              }, function (compare) {
                this.compare = compare;
                this.convertor = new _.org.kevoree.modeling.api.trace.Event2Trace(this.compare);
                this.currentModel = null;
                this.invertedTraceSequence = null;
                this.traceSequence = null;
                this.activated = true;
              }, /** @lends _.org.kevoree.modeling.api.util.ModelTracker.prototype */ {
                elementChanged: function (evt) {
                  if (this.activated) {
                    var tmp$0, tmp$1;
                    ((tmp$0 = this.traceSequence) != null ? tmp$0 : Kotlin.throwNPE()).append(this.convertor.convert(evt));
                    ((tmp$1 = this.invertedTraceSequence) != null ? tmp$1 : Kotlin.throwNPE()).append(this.convertor.inverse(evt));
                  }
                },
                track: function (model) {
                  this.currentModel = model;
                  var tmp$0;
                  ((tmp$0 = this.currentModel) != null ? tmp$0 : Kotlin.throwNPE()).addModelTreeListener(this);
                  this.traceSequence = new _.org.kevoree.modeling.api.trace.TraceSequence(this.compare.factory);
                  this.invertedTraceSequence = new _.org.kevoree.modeling.api.trace.TraceSequence(this.compare.factory);
                },
                untrack: function () {
                  var tmp$0;
                  (tmp$0 = this.currentModel) != null ? tmp$0.removeModelTreeListener(this) : null;
                },
                redo: function () {
                  if (this.currentModel != null && this.traceSequence != null) {
                    this.activated = false;
                    try {
                      var tmp$0, tmp$1;
                      ((tmp$0 = this.traceSequence) != null ? tmp$0 : Kotlin.throwNPE()).applyOn((tmp$1 = this.currentModel) != null ? tmp$1 : Kotlin.throwNPE());
                    }
                    finally {
                      this.activated = true;
                    }
                  }
                },
                undo: function () {
                  if (this.currentModel != null && this.invertedTraceSequence != null) {
                    this.activated = false;
                    var tmp$0;
                    ((tmp$0 = this.invertedTraceSequence) != null ? tmp$0 : Kotlin.throwNPE()).reverse();
                    try {
                      var tmp$1, tmp$2;
                      ((tmp$1 = this.invertedTraceSequence) != null ? tmp$1 : Kotlin.throwNPE()).applyOn((tmp$2 = this.currentModel) != null ? tmp$2 : Kotlin.throwNPE());
                    }
                    finally {
                      var tmp$3;
                      ((tmp$3 = this.invertedTraceSequence) != null ? tmp$3 : Kotlin.throwNPE()).reverse();
                      this.activated = true;
                    }
                  }
                },
                reset: function () {
                  this.traceSequence = new _.org.kevoree.modeling.api.trace.TraceSequence(this.compare.factory);
                  this.invertedTraceSequence = new _.org.kevoree.modeling.api.trace.TraceSequence(this.compare.factory);
                }
              }),
              ModelAttributeVisitor: Kotlin.createTrait(null),
              ActionType: Kotlin.createClass(function () {
                return [Kotlin.Enum];
              }, function $fun(code) {
                $fun.baseInitializer.call(this);
                this.code = code;
              }, null, /** @lends _.org.kevoree.modeling.api.util.ActionType */ {
                object_initializer$: function () {
                  return Kotlin.createEnumEntries({
                    SET: new _.org.kevoree.modeling.api.util.ActionType('S'),
                    ADD: new _.org.kevoree.modeling.api.util.ActionType('a'),
                    REMOVE: new _.org.kevoree.modeling.api.util.ActionType('r'),
                    ADD_ALL: new _.org.kevoree.modeling.api.util.ActionType('A'),
                    REMOVE_ALL: new _.org.kevoree.modeling.api.util.ActionType('R'),
                    RENEW_INDEX: new _.org.kevoree.modeling.api.util.ActionType('I'),
                    CONTROL: new _.org.kevoree.modeling.api.util.ActionType('C')
                  });
                }
              })
            }),
            json: Kotlin.definePackage(function () {
              this.Type = Kotlin.createObject(null, function () {
                this.VALUE = 0;
                this.LEFT_BRACE = 1;
                this.RIGHT_BRACE = 2;
                this.LEFT_BRACKET = 3;
                this.RIGHT_BRACKET = 4;
                this.COMMA = 5;
                this.COLON = 6;
                this.EOF = 42;
              });
              this.JSONString = Kotlin.createObject(null, function () {
                this.escapeChar_iwx5i$ = '\\';
              }, {
                encodeBuffer: function (buffer, chain) {
                  if (chain == null) {
                    return;
                  }
                  var i = 0;
                  while (i < chain.length) {
                    var ch = chain.charAt(i);
                    if (ch === '"') {
                      buffer.append(this.escapeChar_iwx5i$);
                      buffer.append('"');
                    }
                     else if (ch === this.escapeChar_iwx5i$) {
                      buffer.append(this.escapeChar_iwx5i$);
                      buffer.append(this.escapeChar_iwx5i$);
                    }
                     else if (ch === '\n') {
                      buffer.append(this.escapeChar_iwx5i$);
                      buffer.append('n');
                    }
                     else if (ch === '\r') {
                      buffer.append(this.escapeChar_iwx5i$);
                      buffer.append('r');
                    }
                     else if (ch === '\t') {
                      buffer.append(this.escapeChar_iwx5i$);
                      buffer.append('t');
                    }
                     else if (ch === '\u2028') {
                      buffer.append(this.escapeChar_iwx5i$);
                      buffer.append('u');
                      buffer.append('2');
                      buffer.append('0');
                      buffer.append('2');
                      buffer.append('8');
                    }
                     else if (ch === '\u2029') {
                      buffer.append(this.escapeChar_iwx5i$);
                      buffer.append('u');
                      buffer.append('2');
                      buffer.append('0');
                      buffer.append('2');
                      buffer.append('9');
                    }
                     else {
                      buffer.append(ch);
                    }
                    i = i + 1;
                  }
                },
                encode: function (ostream, chain) {
                  if (chain == null) {
                    return;
                  }
                  var i = 0;
                  while (i < chain.length) {
                    var ch = chain.charAt(i);
                    if (ch === '"') {
                      ostream.print_1(this.escapeChar_iwx5i$);
                      ostream.print_1('"');
                    }
                     else if (ch === this.escapeChar_iwx5i$) {
                      ostream.print_1(this.escapeChar_iwx5i$);
                      ostream.print_1(this.escapeChar_iwx5i$);
                    }
                     else if (ch === '\n') {
                      ostream.print_1(this.escapeChar_iwx5i$);
                      ostream.print_1('n');
                    }
                     else if (ch === '\r') {
                      ostream.print_1(this.escapeChar_iwx5i$);
                      ostream.print_1('r');
                    }
                     else if (ch === '\t') {
                      ostream.print_1(this.escapeChar_iwx5i$);
                      ostream.print_1('t');
                    }
                     else if (ch === '\u2028') {
                      ostream.print_1(this.escapeChar_iwx5i$);
                      ostream.print_1('u');
                      ostream.print_1('2');
                      ostream.print_1('0');
                      ostream.print_1('2');
                      ostream.print_1('8');
                    }
                     else if (ch === '\u2029') {
                      ostream.print_1(this.escapeChar_iwx5i$);
                      ostream.print_1('u');
                      ostream.print_1('2');
                      ostream.print_1('0');
                      ostream.print_1('2');
                      ostream.print_1('9');
                    }
                     else {
                      ostream.print_1(ch);
                    }
                    i = i + 1;
                  }
                },
                unescape: function (src) {
                  if (src == null) {
                    return null;
                  }
                  if (src.length === 0) {
                    return src;
                  }
                  var builder = null;
                  var i = 0;
                  while (i < src.length) {
                    var current = src.charAt(i);
                    if (current === this.escapeChar_iwx5i$) {
                      if (builder == null) {
                        builder = new Kotlin.StringBuilder();
                        builder != null ? builder.append(src.substring(0, i)) : null;
                      }
                      i++;
                      var current2 = src.charAt(i);
                      {
                        if (current2 === '"') {
                          builder != null ? builder.append('"') : null;
                        }
                         else if (current2 === '\\') {
                          builder != null ? builder.append(current2) : null;
                        }
                         else if (current2 === '/') {
                          builder != null ? builder.append(current2) : null;
                        }
                         else if (current2 === 'b') {
                          builder != null ? builder.append('\b') : null;
                        }
                         else if (current2 === 'f') {
                          builder != null ? builder.append((12).toChar()) : null;
                        }
                         else if (current2 === 'n') {
                          builder != null ? builder.append('\n') : null;
                        }
                         else if (current2 === 'r') {
                          builder != null ? builder.append('\r') : null;
                        }
                         else if (current2 === 't') {
                          builder != null ? builder.append('\t') : null;
                        }
                         else if (current2 === 'u') {
                          throw new Error('Bad char to escape ');
                        }
                      }
                    }
                     else {
                      if (builder != null) {
                        builder = builder != null ? builder.append(current) : null;
                      }
                    }
                    i++;
                  }
                  if (builder != null) {
                    return (builder != null ? builder : Kotlin.throwNPE()).toString();
                  }
                   else {
                    return src;
                  }
                }
              });
            }, /** @lends _.org.kevoree.modeling.api.json */ {
              Token: Kotlin.createClass(null, function (tokenType, value) {
                this.tokenType = tokenType;
                this.value = value;
              }, /** @lends _.org.kevoree.modeling.api.json.Token.prototype */ {
                toString: function () {
                  var tmp$0;
                  if (this.value != null) {
                    tmp$0 = ' (' + this.value + ')';
                  }
                   else {
                    tmp$0 = '';
                  }
                  var v = tmp$0;
                  var result = this.tokenType.toString() + v;
                  return result;
                }
              }),
              Lexer: Kotlin.createClass(null, function (inputStream) {
                this.inputStream = inputStream;
                this.bytes = this.inputStream.readBytes();
                this.EOF = new _.org.kevoree.modeling.api.json.Token(_.org.kevoree.modeling.api.json.Type.EOF, null);
                this.index = 0;
                this.BOOLEAN_LETTERS = null;
                this.DIGIT = null;
              }, /** @lends _.org.kevoree.modeling.api.json.Lexer.prototype */ {
                isSpace: function (c) {
                  return c === ' ' || c === '\r' || c === '\n' || c === '\t';
                },
                nextChar: function () {
                  return _.org.kevoree.modeling.api.util.ByteConverter.toChar(this.bytes[this.index++]);
                },
                peekChar: function () {
                  return _.org.kevoree.modeling.api.util.ByteConverter.toChar(this.bytes[this.index]);
                },
                isDone: function () {
                  return this.index >= this.bytes.length;
                },
                isBooleanLetter: function (c) {
                  if (this.BOOLEAN_LETTERS == null) {
                    this.BOOLEAN_LETTERS = new Kotlin.PrimitiveHashSet();
                    var tmp$0, tmp$1, tmp$2, tmp$3, tmp$4, tmp$5, tmp$6, tmp$7;
                    ((tmp$0 = this.BOOLEAN_LETTERS) != null ? tmp$0 : Kotlin.throwNPE()).add_za3rmp$('f');
                    ((tmp$1 = this.BOOLEAN_LETTERS) != null ? tmp$1 : Kotlin.throwNPE()).add_za3rmp$('a');
                    ((tmp$2 = this.BOOLEAN_LETTERS) != null ? tmp$2 : Kotlin.throwNPE()).add_za3rmp$('l');
                    ((tmp$3 = this.BOOLEAN_LETTERS) != null ? tmp$3 : Kotlin.throwNPE()).add_za3rmp$('s');
                    ((tmp$4 = this.BOOLEAN_LETTERS) != null ? tmp$4 : Kotlin.throwNPE()).add_za3rmp$('e');
                    ((tmp$5 = this.BOOLEAN_LETTERS) != null ? tmp$5 : Kotlin.throwNPE()).add_za3rmp$('t');
                    ((tmp$6 = this.BOOLEAN_LETTERS) != null ? tmp$6 : Kotlin.throwNPE()).add_za3rmp$('r');
                    ((tmp$7 = this.BOOLEAN_LETTERS) != null ? tmp$7 : Kotlin.throwNPE()).add_za3rmp$('u');
                  }
                  var tmp$8;
                  return ((tmp$8 = this.BOOLEAN_LETTERS) != null ? tmp$8 : Kotlin.throwNPE()).contains_za3rmp$(c);
                },
                isDigit: function (c) {
                  if (this.DIGIT == null) {
                    this.DIGIT = new Kotlin.PrimitiveHashSet();
                    var tmp$0, tmp$1, tmp$2, tmp$3, tmp$4, tmp$5, tmp$6, tmp$7, tmp$8, tmp$9;
                    ((tmp$0 = this.DIGIT) != null ? tmp$0 : Kotlin.throwNPE()).add_za3rmp$('0');
                    ((tmp$1 = this.DIGIT) != null ? tmp$1 : Kotlin.throwNPE()).add_za3rmp$('1');
                    ((tmp$2 = this.DIGIT) != null ? tmp$2 : Kotlin.throwNPE()).add_za3rmp$('2');
                    ((tmp$3 = this.DIGIT) != null ? tmp$3 : Kotlin.throwNPE()).add_za3rmp$('3');
                    ((tmp$4 = this.DIGIT) != null ? tmp$4 : Kotlin.throwNPE()).add_za3rmp$('4');
                    ((tmp$5 = this.DIGIT) != null ? tmp$5 : Kotlin.throwNPE()).add_za3rmp$('5');
                    ((tmp$6 = this.DIGIT) != null ? tmp$6 : Kotlin.throwNPE()).add_za3rmp$('6');
                    ((tmp$7 = this.DIGIT) != null ? tmp$7 : Kotlin.throwNPE()).add_za3rmp$('7');
                    ((tmp$8 = this.DIGIT) != null ? tmp$8 : Kotlin.throwNPE()).add_za3rmp$('8');
                    ((tmp$9 = this.DIGIT) != null ? tmp$9 : Kotlin.throwNPE()).add_za3rmp$('9');
                  }
                  var tmp$10;
                  return ((tmp$10 = this.DIGIT) != null ? tmp$10 : Kotlin.throwNPE()).contains_za3rmp$(c);
                },
                isValueLetter: function (c) {
                  return c === '-' || c === '+' || c === '.' || this.isDigit(c) || this.isBooleanLetter(c);
                },
                nextToken: function () {
                  if (this.isDone()) {
                    return this.EOF;
                  }
                  var tokenType = _.org.kevoree.modeling.api.json.Type.EOF;
                  var c = this.nextChar();
                  var currentValue = new Kotlin.StringBuilder();
                  var jsonValue = null;
                  while (!this.isDone() && this.isSpace(c)) {
                    c = this.nextChar();
                  }
                  if ('"' === c) {
                    tokenType = _.org.kevoree.modeling.api.json.Type.VALUE;
                    if (!this.isDone()) {
                      c = this.nextChar();
                      while (this.index < this.bytes.length && c !== '"') {
                        currentValue.append(c);
                        if (c === '\\' && this.index < this.bytes.length) {
                          c = this.nextChar();
                          currentValue.append(c);
                        }
                        c = this.nextChar();
                      }
                      jsonValue = currentValue.toString();
                    }
                     else {
                      throw new Kotlin.RuntimeException('Unterminated string');
                    }
                  }
                   else if ('{' === c) {
                    tokenType = _.org.kevoree.modeling.api.json.Type.LEFT_BRACE;
                  }
                   else if ('}' === c) {
                    tokenType = _.org.kevoree.modeling.api.json.Type.RIGHT_BRACE;
                  }
                   else if ('[' === c) {
                    tokenType = _.org.kevoree.modeling.api.json.Type.LEFT_BRACKET;
                  }
                   else if (']' === c) {
                    tokenType = _.org.kevoree.modeling.api.json.Type.RIGHT_BRACKET;
                  }
                   else if (':' === c) {
                    tokenType = _.org.kevoree.modeling.api.json.Type.COLON;
                  }
                   else if (',' === c) {
                    tokenType = _.org.kevoree.modeling.api.json.Type.COMMA;
                  }
                   else if (!this.isDone()) {
                    while (this.isValueLetter(c)) {
                      currentValue.append(c);
                      if (!this.isValueLetter(this.peekChar())) {
                        break;
                      }
                       else {
                        c = this.nextChar();
                      }
                    }
                    var v = currentValue.toString();
                    if (Kotlin.equals('true', v.toLowerCase())) {
                      jsonValue = true;
                    }
                     else if (Kotlin.equals('false', v.toLowerCase())) {
                      jsonValue = false;
                    }
                     else {
                      jsonValue = v.toLowerCase();
                    }
                    tokenType = _.org.kevoree.modeling.api.json.Type.VALUE;
                  }
                   else {
                    tokenType = _.org.kevoree.modeling.api.json.Type.EOF;
                  }
                  return new _.org.kevoree.modeling.api.json.Token(tokenType, jsonValue);
                }
              }),
              JSONModelLoader: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.ModelLoader];
              }, function (factory) {
                this.factory = factory;
              }, /** @lends _.org.kevoree.modeling.api.json.JSONModelLoader.prototype */ {
                loadModelFromString: function (str) {
                  return this.deserialize(_.org.kevoree.modeling.api.util.ByteConverter.byteArrayInputStreamFromString(str));
                },
                loadModelFromStream: function (inputStream) {
                  return this.deserialize(inputStream);
                },
                deserialize: function (instream) {
                  if (instream == null) {
                    throw new Error('Null input Stream');
                  }
                  var resolverCommands = new Kotlin.ArrayList();
                  var roots = new Kotlin.ArrayList();
                  var lexer = new _.org.kevoree.modeling.api.json.Lexer(instream);
                  var currentToken = lexer.nextToken();
                  if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.LEFT_BRACE) {
                    this.loadObject(lexer, null, null, roots, resolverCommands);
                  }
                   else {
                    throw new Error('Bad Format / {\xA0expected');
                  }
                  {
                    var tmp$0 = resolverCommands.iterator();
                    while (tmp$0.hasNext()) {
                      var resol = tmp$0.next();
                      resol.run();
                    }
                  }
                  return roots;
                },
                loadObject: function (lexer, nameInParent, parent, roots, commands) {
                  var currentToken = lexer.nextToken();
                  var currentObject = null;
                  if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.VALUE) {
                    if (Kotlin.equals(currentToken.value, 'class')) {
                      lexer.nextToken();
                      currentToken = lexer.nextToken();
                      var tmp$0, tmp$1;
                      var name = (tmp$1 = (tmp$0 = currentToken.value) != null ? tmp$0.toString() : null) != null ? tmp$1 : Kotlin.throwNPE();
                      var typeName = null;
                      var isRoot = false;
                      if (name.startsWith('root:')) {
                        isRoot = true;
                        name = name.substring('root:'.length);
                      }
                      if (name.contains('@')) {
                        typeName = name.substring(0, name.indexOf('@'));
                        var key = name.substring(name.indexOf('@') + 1);
                        if (parent == null) {
                          if (isRoot) {
                            currentObject = this.factory.lookup('/');
                          }
                        }
                         else {
                          var path = parent.path() + '/' + nameInParent + '[' + key + ']';
                          currentObject = this.factory.lookup(path);
                        }
                      }
                       else {
                        typeName = name;
                      }
                      if (currentObject == null) {
                        currentObject = this.factory.create(typeName != null ? typeName : Kotlin.throwNPE());
                      }
                      if (isRoot) {
                        this.factory.root(currentObject != null ? currentObject : Kotlin.throwNPE());
                      }
                      if (parent == null) {
                        roots.add_za3rmp$(currentObject != null ? currentObject : Kotlin.throwNPE());
                      }
                      var currentNameAttOrRef = null;
                      var refModel = false;
                      currentToken = lexer.nextToken();
                      while (currentToken.tokenType !== _.org.kevoree.modeling.api.json.Type.EOF) {
                        if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.LEFT_BRACE) {
                          this.loadObject(lexer, currentNameAttOrRef != null ? currentNameAttOrRef : Kotlin.throwNPE(), currentObject, roots, commands);
                        }
                        if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.COMMA) {
                        }
                        if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.VALUE) {
                          if (currentNameAttOrRef == null) {
                            currentNameAttOrRef = Kotlin.toString(currentToken.value);
                          }
                           else {
                            if (refModel) {
                              var tmp$2;
                              commands.add_za3rmp$(new _.org.kevoree.modeling.api.json.ResolveCommand(roots, ((tmp$2 = currentToken.value) != null ? tmp$2 : Kotlin.throwNPE()).toString(), currentObject != null ? currentObject : Kotlin.throwNPE(), currentNameAttOrRef != null ? currentNameAttOrRef : Kotlin.throwNPE()));
                            }
                             else {
                              var unscaped = _.org.kevoree.modeling.api.json.JSONString.unescape(Kotlin.toString(currentToken.value));
                              (currentObject != null ? currentObject : Kotlin.throwNPE()).reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.SET, currentNameAttOrRef != null ? currentNameAttOrRef : Kotlin.throwNPE(), unscaped, false, false);
                              currentNameAttOrRef = null;
                            }
                          }
                        }
                        if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.LEFT_BRACKET) {
                          currentToken = lexer.nextToken();
                          if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.LEFT_BRACE) {
                            this.loadObject(lexer, currentNameAttOrRef != null ? currentNameAttOrRef : Kotlin.throwNPE(), currentObject, roots, commands);
                          }
                           else {
                            refModel = true;
                            if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.VALUE) {
                              var tmp$3;
                              commands.add_za3rmp$(new _.org.kevoree.modeling.api.json.ResolveCommand(roots, ((tmp$3 = currentToken.value) != null ? tmp$3 : Kotlin.throwNPE()).toString(), currentObject != null ? currentObject : Kotlin.throwNPE(), currentNameAttOrRef != null ? currentNameAttOrRef : Kotlin.throwNPE()));
                            }
                          }
                        }
                        if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.RIGHT_BRACKET) {
                          currentNameAttOrRef = null;
                          refModel = false;
                        }
                        if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.RIGHT_BRACE) {
                          if (parent != null) {
                            parent.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, nameInParent != null ? nameInParent : Kotlin.throwNPE(), currentObject, false, false);
                          }
                          return;
                        }
                        currentToken = lexer.nextToken();
                      }
                    }
                     else {
                      throw new Error('Bad Format / eClass att must be first');
                    }
                  }
                   else {
                    throw new Error('Bad Format');
                  }
                }
              }),
              ResolveCommand: Kotlin.createClass(null, function (roots, ref, currentRootElem, refName) {
                this.roots = roots;
                this.ref = ref;
                this.currentRootElem = currentRootElem;
                this.refName = refName;
              }, /** @lends _.org.kevoree.modeling.api.json.ResolveCommand.prototype */ {
                run: function () {
                  var referencedElement = null;
                  var i = 0;
                  while (referencedElement == null && i < this.roots.size()) {
                    referencedElement = this.roots.get_za3lpa$(i++).findByPath(this.ref);
                  }
                  if (referencedElement != null) {
                    this.currentRootElem.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, this.refName, referencedElement, false, false);
                  }
                   else {
                    throw new Error('Unresolved ' + this.ref);
                  }
                }
              }),
              ModelReferenceVisitor: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.util.ModelVisitor];
              }, function $fun(out) {
                $fun.baseInitializer.call(this);
                this.out = out;
                this.isFirst = true;
              }, /** @lends _.org.kevoree.modeling.api.json.ModelReferenceVisitor.prototype */ {
                beginVisitRef: function (refName, refType) {
                  this.out.print_4(',"' + refName + '":[');
                  this.isFirst = true;
                  return true;
                },
                endVisitRef: function (refName) {
                  this.out.print_4(']');
                },
                visit: function (elem, refNameInParent, parent) {
                  if (!this.isFirst) {
                    this.out.print_4(',');
                  }
                   else {
                    this.isFirst = false;
                  }
                  this.out.print_4('"' + elem.path() + '"');
                }
              }),
              JSONModelSerializer: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.ModelSerializer];
              }, null, /** @lends _.org.kevoree.modeling.api.json.JSONModelSerializer.prototype */ {
                serialize: function (model) {
                  var outstream = new _.java.io.ByteArrayOutputStream();
                  this.serializeToStream(model, outstream);
                  outstream.close();
                  return outstream.toString();
                },
                serializeToStream: function (model, raw) {
                  var out = new _.java.io.PrintStream(new _.java.io.BufferedOutputStream(raw), false);
                  var internalReferenceVisitor = new _.org.kevoree.modeling.api.json.ModelReferenceVisitor(out);
                  var masterVisitor = _.org.kevoree.modeling.api.json.JSONModelSerializer.serializeToStream$f(out, this, internalReferenceVisitor);
                  model.visit(masterVisitor, true, true, false);
                  out.flush();
                },
                printAttName: function (elem, out) {
                  var isRoot = '';
                  if (Kotlin.equals(elem.path(), '/')) {
                    isRoot = 'root:';
                  }
                  out.print_4('\n{"class":"' + isRoot + elem.metaClassName() + '@' + elem.internalGetKey() + '"');
                  var attributeVisitor = _.org.kevoree.modeling.api.json.JSONModelSerializer.printAttName$f(out);
                  elem.visitAttributes(attributeVisitor);
                }
              }, /** @lends _.org.kevoree.modeling.api.json.JSONModelSerializer */ {
                serializeToStream$f: function (out, this$JSONModelSerializer, internalReferenceVisitor) {
                  return Kotlin.createObject(function () {
                    return [_.org.kevoree.modeling.api.util.ModelVisitor];
                  }, function $fun() {
                    $fun.baseInitializer.call(this);
                    this.isFirstInRef = true;
                  }, {
                    beginVisitElem: function (elem) {
                      if (!this.isFirstInRef) {
                        out.print_4(',');
                        this.isFirstInRef = false;
                      }
                      this$JSONModelSerializer.printAttName(elem, out);
                      var tmp$0;
                      (tmp$0 = internalReferenceVisitor.alreadyVisited) != null ? tmp$0.clear() : null;
                      elem.visit(internalReferenceVisitor, false, false, true);
                    },
                    endVisitElem: function (elem) {
                      out.println_2('}');
                      this.isFirstInRef = false;
                    },
                    beginVisitRef: function (refName, refType) {
                      out.print_4(',"' + refName + '":[');
                      this.isFirstInRef = true;
                      return true;
                    },
                    endVisitRef: function (refName) {
                      out.print_4(']');
                      this.isFirstInRef = false;
                    },
                    visit: function (elem, refNameInParent, parent) {
                    }
                  });
                },
                printAttName$f: function (out) {
                  return Kotlin.createObject(function () {
                    return [_.org.kevoree.modeling.api.util.ModelAttributeVisitor];
                  }, null, {
                    visit: function (value, name, parent) {
                      if (value != null) {
                        out.print_4(',"' + name + '":"');
                        if (Kotlin.isType(value, Date)) {
                          _.org.kevoree.modeling.api.json.JSONString.encode(out, '' + value.getTime());
                        }
                         else {
                          _.org.kevoree.modeling.api.json.JSONString.encode(out, _.org.kevoree.modeling.api.util.AttConverter.convFlatAtt(value));
                        }
                        out.print_4('"');
                      }
                    }
                  });
                }
              })
            }),
            events: Kotlin.definePackage(null, /** @lends _.org.kevoree.modeling.api.events */ {
              ModelEvent: Kotlin.createClass(null, function (etype, elementAttributeType, elementAttributeName, value, previous_value, source, previousPath) {
                this.etype = etype;
                this.elementAttributeType = elementAttributeType;
                this.elementAttributeName = elementAttributeName;
                this.value = value;
                this.previous_value = previous_value;
                this.source = source;
                this.previousPath = previousPath;
              }, /** @lends _.org.kevoree.modeling.api.events.ModelEvent.prototype */ {
                toString: function () {
                  if (Kotlin.isType(this.source, _.org.kevoree.modeling.api.time.TimeAwareKMFContainer)) {
                    return 'ModelEvent[src:[' + this.source.now + ']' + this.source.path() + ', type:' + this.etype + ', elementAttributeType:' + this.elementAttributeType + ', elementAttributeName:' + this.elementAttributeName + ', value:' + Kotlin.toString(this.value) + ', previousValue:' + Kotlin.toString(this.previous_value) + ']';
                  }
                   else {
                    var tmp$0;
                    return 'ModelEvent[src:' + Kotlin.toString((tmp$0 = this.source) != null ? tmp$0.path() : null) + ', type:' + this.etype + ', elementAttributeType:' + this.elementAttributeType + ', elementAttributeName:' + this.elementAttributeName + ', value:' + Kotlin.toString(this.value) + ', previousValue:' + Kotlin.toString(this.previous_value) + ']';
                  }
                }
              }),
              ModelElementListener: Kotlin.createTrait(null)
            }),
            time: Kotlin.definePackage(function () {
              this.TimeComparator = Kotlin.createObject(null, null, {
                compare: function (a, b) {
                  if (a === b) {
                    return 0;
                  }
                   else {
                    if (a < b) {
                      return -1;
                    }
                     else {
                      return 1;
                    }
                  }
                }
              });
              this.TimeSegmentConst = Kotlin.createObject(null, function () {
                this.GLOBAL_TIMEMETA = '#global';
              });
            }, /** @lends _.org.kevoree.modeling.api.time */ {
              TimeView: Kotlin.createTrait(null),
              TimeAwareKMFContainer: Kotlin.createTrait(function () {
                return [_.org.kevoree.modeling.api.TimedContainer, _.org.kevoree.modeling.api.persistence.KMFContainerProxy];
              }, /** @lends _.org.kevoree.modeling.api.time.TimeAwareKMFContainer.prototype */ {
                meta: {
                  get: function () {
                    return this.$meta_e0ta8m$;
                  },
                  set: function (tmp$0) {
                    this.$meta_e0ta8m$ = tmp$0;
                  }
                },
                getOriginTransaction: function () {
                  var tmp$0;
                  return ((tmp$0 = this.originFactory) != null ? tmp$0 : Kotlin.throwNPE()).originTransaction;
                },
                previous: function () {
                  var previousTime = this.timeTree().previous(this.now);
                  if (previousTime != null) {
                    return this.getOriginTransaction().time(previousTime).lookup(this.path());
                  }
                  return null;
                },
                next: function () {
                  var previousTime = this.timeTree().next(this.now);
                  if (previousTime != null) {
                    return this.getOriginTransaction().time(previousTime).lookup(this.path());
                  }
                  return null;
                },
                last: function () {
                  var tmp$0;
                  var previousTime = (tmp$0 = this.timeTree().versionTree.lastWhileNot(this.now, _.org.kevoree.modeling.api.time.blob.STATE.object.DELETED)) != null ? tmp$0.key : null;
                  if (previousTime != null) {
                    return this.getOriginTransaction().time(previousTime).lookup(this.path());
                  }
                  return null;
                },
                first: function () {
                  var tmp$0;
                  var previousTime = (tmp$0 = this.timeTree().versionTree.firstWhileNot(this.now, _.org.kevoree.modeling.api.time.blob.STATE.object.DELETED)) != null ? tmp$0.key : null;
                  if (previousTime != null) {
                    return this.getOriginTransaction().time(previousTime).lookup(this.path());
                  }
                  return null;
                },
                jump: function (time) {
                  var previousTime = this.timeTree().previous(time);
                  if (previousTime != null) {
                    return this.getOriginTransaction().time(previousTime).lookup(this.path());
                  }
                  return null;
                },
                timeTree: function () {
                  var tmp$0;
                  return ((tmp$0 = this.originFactory) != null ? tmp$0 : Kotlin.throwNPE()).getTimeTree(this.path());
                }
              }),
              TimeTree: Kotlin.createTrait(null),
              TimeAwareKMFFactory: Kotlin.createTrait(function () {
                return [_.org.kevoree.modeling.api.time.TimeView, _.org.kevoree.modeling.api.persistence.PersistenceKMFFactory];
              }, /** @lends _.org.kevoree.modeling.api.time.TimeAwareKMFFactory.prototype */ {
                relativeTime: {
                  get: function () {
                    return this.$relativeTime_53j5cx$;
                  }
                },
                sharedCache: {
                  get: function () {
                    return this.$sharedCache_s3os97$;
                  }
                },
                entitiesCache: {
                  get: function () {
                    return this.$entitiesCache_hk1jbt$;
                  },
                  set: function (tmp$0) {
                    this.$entitiesCache_hk1jbt$ = tmp$0;
                  }
                },
                originTransaction: {
                  get: function () {
                    return this.$originTransaction_8vjs1c$;
                  },
                  set: function (tmp$0) {
                    this.$originTransaction_8vjs1c$ = tmp$0;
                  }
                },
                getEntitiesMeta: function () {
                  if (this.entitiesCache != null) {
                    var tmp$0;
                    return (tmp$0 = this.entitiesCache) != null ? tmp$0 : Kotlin.throwNPE();
                  }
                   else {
                    var payload = this.datastore.get(_.org.kevoree.modeling.api.time.TimeSegment.object.ENTITIES.name(), this.relativeTime.toString());
                    var blob = new _.org.kevoree.modeling.api.time.blob.EntitiesMeta();
                    if (payload != null) {
                      blob.load(payload);
                    }
                    this.entitiesCache = blob;
                    return blob;
                  }
                },
                endCommit: function () {
                  var entitiesMeta = this.getEntitiesMeta();
                  {
                    var tmp$0 = entitiesMeta.list.keySet().iterator();
                    while (tmp$0.hasNext()) {
                      var path = tmp$0.next();
                      var timeTree = this.getTimeTree(path);
                      if (timeTree.dirty) {
                        this.datastore.put(_.org.kevoree.modeling.api.time.TimeSegment.object.TIMEMETA.name(), path, timeTree.toString());
                        timeTree.dirty = false;
                      }
                    }
                  }
                  if (entitiesMeta.isDirty) {
                    this.datastore.put(_.org.kevoree.modeling.api.time.TimeSegment.object.ENTITIES.name(), this.relativeTime.toString(), entitiesMeta.toString());
                    entitiesMeta.isDirty = false;
                  }
                  var globalTime = this.getTimeTree(_.org.kevoree.modeling.api.time.TimeSegmentConst.GLOBAL_TIMEMETA);
                  if (globalTime.dirty) {
                    this.datastore.put(_.org.kevoree.modeling.api.time.TimeSegment.object.TIMEMETA.name(), _.org.kevoree.modeling.api.time.TimeSegmentConst.GLOBAL_TIMEMETA, globalTime.toString());
                    globalTime.dirty = false;
                  }
                  {
                    var tmp$1 = this.elementsToBeRemoved.iterator();
                    while (tmp$1.hasNext()) {
                      var e = tmp$1.next();
                      this.cleanUnusedPaths(e);
                    }
                  }
                  this.elementsToBeRemoved.clear();
                  this.datastore.commit();
                },
                clear: function () {
                  var tmp$0;
                  if (this.entitiesCache != null && ((tmp$0 = this.entitiesCache) != null ? tmp$0 : Kotlin.throwNPE()).isDirty) {
                    Kotlin.println('WARNING :: CLOSED TimeView in dirty mode ! ' + this.relativeTime);
                  }
                  _.org.kevoree.modeling.api.persistence.PersistenceKMFFactory.prototype.clear.call(this);
                  this.entitiesCache = null;
                },
                monitor: function (elem) {
                  if (!this.dirty) {
                    this.dirty = true;
                    var globalTime = this.getTimeTree(_.org.kevoree.modeling.api.time.TimeSegmentConst.GLOBAL_TIMEMETA);
                    if (globalTime.versionTree.lookup(this.relativeTime) == null) {
                      globalTime.versionTree.insert(this.relativeTime, _.org.kevoree.modeling.api.time.blob.STATE.object.EXISTS);
                      globalTime.dirty = true;
                    }
                  }
                  elem.addModelElementListener(this);
                },
                commit: function () {
                  var keys = _.kotlin.toList_h3panj$(this.modified_elements.keySet());
                  {
                    var tmp$0 = keys.iterator();
                    while (tmp$0.hasNext()) {
                      var elem = tmp$0.next();
                      var resolved = this.modified_elements.get_za3rmp$(elem);
                      if (resolved != null) {
                        if (Kotlin.equals(resolved.path(), '')) {
                          if (!resolved.isDeleted()) {
                            resolved.delete();
                          }
                           else {
                            this.modified_elements.remove_za3rmp$(elem);
                          }
                        }
                      }
                    }
                  }
                  {
                    var tmp$1 = this.modified_elements.values().iterator();
                    while (tmp$1.hasNext()) {
                      var elem_0 = tmp$1.next();
                      this.persist(elem_0);
                      this.elementsToBeRemoved.remove_za3rmp$(elem_0.path());
                    }
                  }
                },
                persist: function (elem) {
                  if (Kotlin.isType(elem, _.org.kevoree.modeling.api.persistence.KMFContainerProxy) && !elem.isDirty) {
                    return;
                  }
                  var currentPath = elem.path();
                  if (Kotlin.equals(currentPath, '')) {
                    throw new Error('Internal error, empty path found during persist method ' + elem);
                  }
                  if (!currentPath.startsWith('/')) {
                    throw new Error('Cannot persist, because the path of the element do not refer to a root: ' + currentPath + ' -> ' + elem);
                  }
                  var casted = elem;
                  var traces = elem.toTraces(true, true);
                  var traceSeq = new _.org.kevoree.modeling.api.trace.TraceSequence(this);
                  traceSeq.populate(traces);
                  var entitiesMeta = this.getEntitiesMeta();
                  entitiesMeta.list.put_wn2jw4$(currentPath, true);
                  entitiesMeta.isDirty = true;
                  var key = this.relativeTime.toString() + '/' + currentPath;
                  this.datastore.put(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), key, traceSeq.exportToString());
                  var castedInBounds = elem;
                  var saved = _.org.kevoree.modeling.api.time.blob.MetaHelper.serialize(castedInBounds.internal_inboundReferences);
                  this.datastore.put(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), key + '#', saved);
                  var tmp$0;
                  ((tmp$0 = casted.meta) != null ? tmp$0 : Kotlin.throwNPE()).latestPersisted = this.relativeTime;
                  this.datastore.put(_.org.kevoree.modeling.api.time.TimeSegment.object.ENTITYMETA.name(), key, Kotlin.toString(casted.meta));
                  var timeTree = this.getTimeTree(currentPath);
                  if (timeTree.versionTree.lookup(this.relativeTime) == null) {
                    timeTree.versionTree.insert(this.relativeTime, _.org.kevoree.modeling.api.time.blob.STATE.object.EXISTS);
                    timeTree.dirty = true;
                  }
                },
                remove: function (elem) {
                  if (elem.isDeleted()) {
                    return;
                  }
                  var path = elem.path();
                  if (Kotlin.equals(path, '')) {
                    this.modified_elements.remove_za3rmp$(elem);
                    Kotlin.println("WARNING :: Can't process dangling element! type:" + elem.metaClassName() + ',id=' + elem.internalGetKey() + ' ignored');
                    return;
                  }
                  this.elem_cache.remove_za3rmp$(path);
                  var currentCachedTimeTree = this.getTimeTree(path);
                  currentCachedTimeTree.versionTree.insert(this.relativeTime, _.org.kevoree.modeling.api.time.blob.STATE.object.DELETED);
                  currentCachedTimeTree.dirty = true;
                  var entitiesMeta = this.getEntitiesMeta();
                  entitiesMeta.list.put_wn2jw4$(path, true);
                  entitiesMeta.isDirty = true;
                  if (!this.dirty) {
                    var globalTime = this.getTimeTree(_.org.kevoree.modeling.api.time.TimeSegmentConst.GLOBAL_TIMEMETA);
                    if (globalTime.versionTree.lookup(this.relativeTime) == null) {
                      globalTime.versionTree.insert(this.relativeTime, _.org.kevoree.modeling.api.time.blob.STATE.object.EXISTS);
                      globalTime.dirty = true;
                    }
                  }
                  this.modified_elements.remove_za3rmp$(Kotlin.hashCode(elem).toString());
                },
                cleanUnusedPaths: function (path) {
                  var key = this.relativeTime.toString() + '/' + path;
                  this.datastore.remove(_.org.kevoree.modeling.api.time.TimeSegment.object.ENTITYMETA.name(), key);
                  this.datastore.remove(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), key);
                  this.datastore.remove(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), key + '#');
                },
                getTimeTree: function (path) {
                  var alreadyCached = this.sharedCache.timeCache.get_za3rmp$(path);
                  if (alreadyCached != null) {
                    return alreadyCached;
                  }
                   else {
                    var timeMetaPayLoad = this.datastore.get(_.org.kevoree.modeling.api.time.TimeSegment.object.TIMEMETA.name(), path);
                    var blob = new _.org.kevoree.modeling.api.time.blob.TimeMeta();
                    if (timeMetaPayLoad != null) {
                      blob.load(timeMetaPayLoad);
                    }
                    this.sharedCache.timeCache.put_wn2jw4$(path, blob);
                    return blob;
                  }
                },
                lookup: function (path) {
                  var timeTree = this.getTimeTree(path);
                  var askedTimeResult = timeTree.versionTree.previousOrEqual(this.relativeTime);
                  var askedTime = askedTimeResult != null ? askedTimeResult.key : null;
                  if (askedTime == null || Kotlin.equals((askedTimeResult != null ? askedTimeResult : Kotlin.throwNPE()).value, _.org.kevoree.modeling.api.time.blob.STATE.object.DELETED)) {
                    return null;
                  }
                  var composedKey = Kotlin.toString(askedTime) + '/' + path;
                  if (this.elem_cache.containsKey_za3rmp$(composedKey)) {
                    return this.elem_cache.get_za3rmp$(composedKey);
                  }
                  var metaPayload = this.datastore.get(_.org.kevoree.modeling.api.time.TimeSegment.object.ENTITYMETA.name(), composedKey);
                  if (metaPayload == null) {
                    return null;
                  }
                  var meta = new _.org.kevoree.modeling.api.time.blob.EntityMeta();
                  meta.load(metaPayload);
                  if (meta.metatype != null) {
                    var tmp$0, tmp$1;
                    var elem = (tmp$1 = this.create((tmp$0 = meta.metatype) != null ? tmp$0 : Kotlin.throwNPE())) != null ? tmp$1 : Kotlin.throwNPE();
                    elem.meta = meta;
                    this.elem_cache.put_wn2jw4$(composedKey, elem);
                    elem.isResolved = false;
                    elem.now = askedTime;
                    elem.setOriginPath(path);
                    this.monitor(elem);
                    return elem;
                  }
                   else {
                    throw new Error('Empty Type Name for ' + path);
                  }
                },
                getTraces: function (origin) {
                  var currentPath = origin.path();
                  var sequence = new _.org.kevoree.modeling.api.trace.TraceSequence(this);
                  var castedOrigin = origin;
                  var tmp$0, tmp$1;
                  if (((tmp$0 = castedOrigin.meta) != null ? tmp$0 : Kotlin.throwNPE()).latestPersisted == null) {
                    return null;
                  }
                  var traces = this.datastore.get(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), Kotlin.toString(((tmp$1 = castedOrigin.meta) != null ? tmp$1 : Kotlin.throwNPE()).latestPersisted) + '/' + currentPath);
                  if (traces != null) {
                    sequence.populateFromString(traces != null ? traces : Kotlin.throwNPE());
                    return sequence;
                  }
                  return null;
                },
                now: function () {
                  return this.relativeTime;
                },
                modified: function () {
                  return this.getEntitiesMeta().list.keySet();
                },
                loadInbounds: function (elem) {
                  var castedInBounds = elem;
                  var casted2 = elem;
                  var tmp$0;
                  var payload = this.datastore.get(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), Kotlin.toString(((tmp$0 = casted2.meta) != null ? tmp$0 : Kotlin.throwNPE()).latestPersisted) + '/' + elem.path() + '#');
                  if (payload != null) {
                    castedInBounds.internal_inboundReferences = _.org.kevoree.modeling.api.time.blob.MetaHelper.unserialize(payload, this);
                  }
                },
                delete: function () {
                  {
                    var tmp$0 = this.getEntitiesMeta().list.keySet().iterator();
                    while (tmp$0.hasNext()) {
                      var path = tmp$0.next();
                      var timeMeta = this.getTimeTree(path);
                      timeMeta.versionTree.delete(this.relativeTime);
                      timeMeta.dirty = true;
                      this.elementsToBeRemoved.add_za3rmp$(path);
                    }
                  }
                  this.getEntitiesMeta().list.clear();
                  this.getEntitiesMeta().isDirty = true;
                  if (!this.dirty) {
                    var globalTime = this.getTimeTree(_.org.kevoree.modeling.api.time.TimeSegmentConst.GLOBAL_TIMEMETA);
                    if (globalTime.versionTree.lookup(this.relativeTime) == null) {
                      globalTime.versionTree.insert(this.relativeTime, _.org.kevoree.modeling.api.time.blob.STATE.object.EXISTS);
                      globalTime.dirty = true;
                    }
                  }
                  var entitiesMeta = this.getEntitiesMeta();
                  entitiesMeta.list.clear();
                  entitiesMeta.isDirty = true;
                },
                diff: function (other) {
                  var casted = other;
                  var sequence = new _.org.kevoree.modeling.api.trace.TraceSequence(this);
                  var globalTime = this.getTimeTree(_.org.kevoree.modeling.api.time.TimeSegmentConst.GLOBAL_TIMEMETA);
                  var tmp$0, tmp$1;
                  var resolved1 = (tmp$0 = globalTime.versionTree.previousOrEqual(this.relativeTime)) != null ? tmp$0.key : null;
                  var resolved2 = (tmp$1 = globalTime.versionTree.previousOrEqual(casted.relativeTime)) != null ? tmp$1.key : null;
                  if (resolved1 == null || resolved2 == null) {
                    return sequence;
                  }
                   else {
                    if (_.org.kevoree.modeling.api.time.TimeComparator.compare(resolved1 != null ? resolved1 : Kotlin.throwNPE(), resolved2 != null ? resolved2 : Kotlin.throwNPE()) > 1) {
                      var temp = resolved1;
                      resolved1 = resolved2;
                      resolved2 = temp;
                    }
                  }
                  var currentTP = resolved1 != null ? resolved1 : Kotlin.throwNPE();
                  while (!Kotlin.equals(currentTP, resolved2 != null ? resolved2 : Kotlin.throwNPE())) {
                    var otherEntities = casted.getEntitiesMeta();
                    {
                      var tmp$2 = otherEntities.list.keySet().iterator();
                      while (tmp$2.hasNext()) {
                        var path = tmp$2.next();
                        var key = currentTP.toString() + '/' + path;
                        var raw = this.datastore.get(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), key);
                        if (raw != null) {
                          sequence.populateFromString(raw);
                        }
                      }
                    }
                    var tmp$3, tmp$4;
                    currentTP = (tmp$4 = (tmp$3 = globalTime.versionTree.next(currentTP)) != null ? tmp$3.key : null) != null ? tmp$4 : Kotlin.throwNPE();
                  }
                  return sequence;
                }
              }),
              TimeWalker: Kotlin.createTrait(null),
              TimeSegment: Kotlin.createClass(function () {
                return [Kotlin.Enum];
              }, function $fun() {
                $fun.baseInitializer.call(this);
              }, null, /** @lends _.org.kevoree.modeling.api.time.TimeSegment */ {
                object_initializer$: function () {
                  return Kotlin.createEnumEntries({
                    RAW: new _.org.kevoree.modeling.api.time.TimeSegment(),
                    ENTITYMETA: new _.org.kevoree.modeling.api.time.TimeSegment(),
                    TIMEMETA: new _.org.kevoree.modeling.api.time.TimeSegment(),
                    ENTITIES: new _.org.kevoree.modeling.api.time.TimeSegment()
                  });
                }
              }),
              blob: Kotlin.definePackage(function () {
                this.MetaHelper = Kotlin.createObject(null, function () {
                  this.sep = '#';
                  this.sep2 = '%';
                }, {
                  serialize: function (p) {
                    var buffer = new Kotlin.StringBuilder();
                    var isFirst = true;
                    {
                      var tmp$0 = p.keySet().iterator();
                      while (tmp$0.hasNext()) {
                        var key = tmp$0.next();
                        var tmp$1;
                        var v = (tmp$1 = p.get_za3rmp$(key)) != null ? tmp$1 : Kotlin.throwNPE();
                        if (!isFirst) {
                          buffer.append(this.sep);
                        }
                        buffer.append(key.path());
                        if (v.size() !== 0) {
                          {
                            var tmp$2 = v.iterator();
                            while (tmp$2.hasNext()) {
                              var v2 = tmp$2.next();
                              buffer.append(this.sep2);
                              buffer.append(v2);
                            }
                          }
                        }
                        isFirst = false;
                      }
                    }
                    return buffer.toString();
                  },
                  unserialize: function (p, factory) {
                    var result = new Kotlin.ComplexHashMap();
                    var lines = Kotlin.splitString(p, this.sep);
                    var tmp$0, tmp$1, tmp$2;
                    {
                      tmp$0 = lines, tmp$1 = tmp$0.length;
                      for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
                        var l = tmp$0[tmp$2];
                        var elems = Kotlin.splitString(l, this.sep2);
                        if (elems.length > 1) {
                          var payload = new Kotlin.PrimitiveHashSet();
                          var tmp$3, tmp$4;
                          {
                            tmp$3 = elems.length - 1 + 1;
                            for (var i = 1; i !== tmp$3; i++) {
                              payload.add_za3rmp$(elems[i]);
                            }
                          }
                          result.put_wn2jw4$((tmp$4 = factory.lookup(elems[0])) != null ? tmp$4 : Kotlin.throwNPE(), payload);
                        }
                      }
                    }
                    return result;
                  }
                });
                this.RBCONST = Kotlin.createObject(null, function () {
                  this.BLACK_DELETE = '0';
                  this.BLACK_EXISTS = '1';
                  this.RED_DELETE = '2';
                  this.RED_EXISTS = '3';
                });
              }, /** @lends _.org.kevoree.modeling.api.time.blob */ {
                TimeMeta: Kotlin.createClass(function () {
                  return [_.org.kevoree.modeling.api.time.TimeTree];
                }, function () {
                  this.dirty = true;
                  this.versionTree = new _.org.kevoree.modeling.api.time.blob.RBTree();
                }, /** @lends _.org.kevoree.modeling.api.time.blob.TimeMeta.prototype */ {
                  first: function () {
                    var tmp$0;
                    return (tmp$0 = this.versionTree.first()) != null ? tmp$0.key : null;
                  },
                  last: function () {
                    var tmp$0;
                    return (tmp$0 = this.versionTree.last()) != null ? tmp$0.key : null;
                  },
                  next: function (from) {
                    var tmp$0;
                    return (tmp$0 = this.versionTree.next(from)) != null ? tmp$0.key : null;
                  },
                  previous: function (from) {
                    var tmp$0;
                    return (tmp$0 = this.versionTree.previous(from)) != null ? tmp$0.key : null;
                  },
                  walk: function (walker) {
                    return this.walkAsc(walker);
                  },
                  toString: function () {
                    return this.versionTree.serialize();
                  },
                  load: function (payload) {
                    this.versionTree.unserialize(payload);
                    this.dirty = false;
                  },
                  walkAsc: function (walker) {
                    var elem = this.versionTree.first();
                    while (elem != null) {
                      walker.walk((elem != null ? elem : Kotlin.throwNPE()).key);
                      elem = (elem != null ? elem : Kotlin.throwNPE()).next();
                    }
                  },
                  walkDesc: function (walker) {
                    var elem = this.versionTree.last();
                    while (elem != null) {
                      walker.walk((elem != null ? elem : Kotlin.throwNPE()).key);
                      elem = (elem != null ? elem : Kotlin.throwNPE()).previous();
                    }
                  },
                  walkRangeAsc: function (walker, from, to) {
                    var from2 = from;
                    var to2 = to;
                    if (from > to) {
                      from2 = to;
                      to2 = from;
                    }
                    var elem;
                    elem = this.versionTree.previousOrEqual(from2);
                    while (elem != null) {
                      walker.walk((elem != null ? elem : Kotlin.throwNPE()).key);
                      elem = (elem != null ? elem : Kotlin.throwNPE()).next();
                      if (elem != null) {
                        if ((elem != null ? elem : Kotlin.throwNPE()).key >= to2) {
                          return;
                        }
                      }
                    }
                  },
                  walkRangeDesc: function (walker, from, to) {
                    var from2 = from;
                    var to2 = to;
                    if (from > to) {
                      from2 = to;
                      to2 = from;
                    }
                    var elem;
                    elem = this.versionTree.previousOrEqual(to2);
                    while (elem != null) {
                      walker.walk((elem != null ? elem : Kotlin.throwNPE()).key);
                      elem = (elem != null ? elem : Kotlin.throwNPE()).previous();
                      if (elem != null) {
                        if ((elem != null ? elem : Kotlin.throwNPE()).key <= from2) {
                          walker.walk((elem != null ? elem : Kotlin.throwNPE()).key);
                          return;
                        }
                      }
                    }
                  }
                }, /** @lends _.org.kevoree.modeling.api.time.blob.TimeMeta */ {
                  object_initializer$: function () {
                    return Kotlin.createObject(null, function () {
                      this.GO_DOWN_LEFT = 0;
                      this.GO_DOWN_RIGHT = 1;
                      this.PROCESS_PREFIX = 2;
                      this.PROCESS_INFIX = 3;
                      this.PROCESS_POSTFIX = 4;
                    });
                  }
                }),
                EntitiesMeta: Kotlin.createClass(null, function () {
                  this.isDirty = false;
                  this.sep = '#';
                  this.list = new Kotlin.PrimitiveHashMap();
                }, /** @lends _.org.kevoree.modeling.api.time.blob.EntitiesMeta.prototype */ {
                  toString: function () {
                    var stringBuilder = new Kotlin.StringBuilder();
                    var isFirst = true;
                    {
                      var tmp$0 = this.list.keySet().iterator();
                      while (tmp$0.hasNext()) {
                        var p = tmp$0.next();
                        if (!isFirst) {
                          stringBuilder.append(this.sep);
                        }
                        stringBuilder.append(p);
                        isFirst = false;
                      }
                    }
                    return stringBuilder.toString();
                  },
                  load: function (payload) {
                    if (Kotlin.equals(payload, '')) {
                      return;
                    }
                    var elements = Kotlin.splitString(payload, this.sep);
                    var tmp$0, tmp$1, tmp$2;
                    {
                      tmp$0 = elements, tmp$1 = tmp$0.length;
                      for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
                        var elem = tmp$0[tmp$2];
                        this.list.put_wn2jw4$(elem, true);
                      }
                    }
                    this.isDirty = false;
                  }
                }),
                SharedCache: Kotlin.createClass(null, function () {
                  this.times_ynpjdh$ = new Kotlin.PrimitiveHashMap();
                  this.timeCache = new _.java.util.concurrent.ConcurrentHashMap();
                }, /** @lends _.org.kevoree.modeling.api.time.blob.SharedCache.prototype */ {
                  add: function (tp, tv) {
                    this.times_ynpjdh$.put_wn2jw4$(tp, tv);
                  },
                  get: function (tp) {
                    return this.times_ynpjdh$.get_za3rmp$(tp);
                  },
                  drop: function (tp) {
                    this.times_ynpjdh$.remove_za3rmp$(tp);
                  },
                  keys: function () {
                    return this.times_ynpjdh$.keySet();
                  },
                  flush: function () {
                    this.times_ynpjdh$.clear();
                    this.timeCache.clear();
                  }
                }),
                EntityMeta: Kotlin.createClass(null, function () {
                  this.latestPersisted = null;
                  this.metatype = null;
                  this.sep = '/';
                }, /** @lends _.org.kevoree.modeling.api.time.blob.EntityMeta.prototype */ {
                  toString: function () {
                    var buidler = new Kotlin.StringBuilder();
                    buidler.append(this.latestPersisted);
                    buidler.append(this.sep);
                    buidler.append(this.metatype);
                    return buidler.toString();
                  },
                  load: function (payload) {
                    var elem = Kotlin.splitString(payload, this.sep);
                    if (elem.length === 2) {
                      var originPayload = elem[0];
                      if (!Kotlin.equals(originPayload, '')) {
                        this.latestPersisted = _.java.lang.Long.parseLong(originPayload);
                      }
                      this.metatype = elem[1];
                    }
                     else {
                      throw new Error('Bad EntityTimeMeta format');
                    }
                  }
                }),
                Color: Kotlin.createClass(function () {
                  return [Kotlin.Enum];
                }, function $fun() {
                  $fun.baseInitializer.call(this);
                }, null, /** @lends _.org.kevoree.modeling.api.time.blob.Color */ {
                  object_initializer$: function () {
                    return Kotlin.createEnumEntries({
                      RED: new _.org.kevoree.modeling.api.time.blob.Color(),
                      BLACK: new _.org.kevoree.modeling.api.time.blob.Color()
                    });
                  }
                }),
                STATE: Kotlin.createClass(function () {
                  return [Kotlin.Enum];
                }, function $fun() {
                  $fun.baseInitializer.call(this);
                }, null, /** @lends _.org.kevoree.modeling.api.time.blob.STATE */ {
                  object_initializer$: function () {
                    return Kotlin.createEnumEntries({
                      EXISTS: new _.org.kevoree.modeling.api.time.blob.STATE(),
                      DELETED: new _.org.kevoree.modeling.api.time.blob.STATE()
                    });
                  }
                }),
                Node: Kotlin.createClass(null, function (key, value, color, left, right) {
                  this.key = key;
                  this.value = value;
                  this.color = color;
                  this.left = left;
                  this.right = right;
                  this.parent = null;
                  if (this.left != null) {
                    var tmp$0;
                    ((tmp$0 = this.left) != null ? tmp$0 : Kotlin.throwNPE()).parent = this;
                  }
                  if (this.right != null) {
                    var tmp$1;
                    ((tmp$1 = this.right) != null ? tmp$1 : Kotlin.throwNPE()).parent = this;
                  }
                  this.parent = null;
                }, /** @lends _.org.kevoree.modeling.api.time.blob.Node.prototype */ {
                  grandparent: function () {
                    var tmp$0;
                    return (tmp$0 = this.parent) != null ? tmp$0.parent : null;
                  },
                  sibling: function () {
                    var tmp$0;
                    if (Kotlin.equals(this, (tmp$0 = this.parent) != null ? tmp$0.left : null)) {
                      var tmp$1;
                      return (tmp$1 = this.parent) != null ? tmp$1.right : null;
                    }
                     else {
                      var tmp$2;
                      return (tmp$2 = this.parent) != null ? tmp$2.left : null;
                    }
                  },
                  uncle: function () {
                    var tmp$0;
                    return (tmp$0 = this.parent) != null ? tmp$0.sibling() : null;
                  },
                  serialize: function (builder) {
                    builder.append('|');
                    if (Kotlin.equals(this.value, _.org.kevoree.modeling.api.time.blob.STATE.object.DELETED)) {
                      if (Kotlin.equals(this.color, _.org.kevoree.modeling.api.time.blob.Color.object.BLACK)) {
                        builder.append(_.org.kevoree.modeling.api.time.blob.RBCONST.BLACK_DELETE);
                      }
                       else {
                        builder.append(_.org.kevoree.modeling.api.time.blob.RBCONST.RED_DELETE);
                      }
                    }
                     else {
                      if (Kotlin.equals(this.color, _.org.kevoree.modeling.api.time.blob.Color.object.BLACK)) {
                        builder.append(_.org.kevoree.modeling.api.time.blob.RBCONST.BLACK_EXISTS);
                      }
                       else {
                        builder.append(_.org.kevoree.modeling.api.time.blob.RBCONST.RED_EXISTS);
                      }
                    }
                    builder.append(this.key);
                    if (this.left == null && this.right == null) {
                      builder.append('%');
                    }
                     else {
                      if (this.left != null) {
                        var tmp$0;
                        (tmp$0 = this.left) != null ? tmp$0.serialize(builder) : null;
                      }
                       else {
                        builder.append('#');
                      }
                      if (this.right != null) {
                        var tmp$1;
                        (tmp$1 = this.right) != null ? tmp$1.serialize(builder) : null;
                      }
                       else {
                        builder.append('#');
                      }
                    }
                  },
                  next: function () {
                    var p = this;
                    if ((p != null ? p : Kotlin.throwNPE()).right != null) {
                      var tmp$0;
                      p = (tmp$0 = (p != null ? p : Kotlin.throwNPE()).right) != null ? tmp$0 : Kotlin.throwNPE();
                      while ((p != null ? p : Kotlin.throwNPE()).left != null) {
                        var tmp$1;
                        p = (tmp$1 = (p != null ? p : Kotlin.throwNPE()).left) != null ? tmp$1 : Kotlin.throwNPE();
                      }
                      return p;
                    }
                     else {
                      if ((p != null ? p : Kotlin.throwNPE()).parent != null) {
                        var tmp$2;
                        if (Kotlin.equals(p, ((tmp$2 = (p != null ? p : Kotlin.throwNPE()).parent) != null ? tmp$2 : Kotlin.throwNPE()).left)) {
                          var tmp$3;
                          return (tmp$3 = (p != null ? p : Kotlin.throwNPE()).parent) != null ? tmp$3 : Kotlin.throwNPE();
                        }
                         else {
                          var tmp$4;
                          while ((p != null ? p : Kotlin.throwNPE()).parent != null && Kotlin.equals(p, ((tmp$4 = (p != null ? p : Kotlin.throwNPE()).parent) != null ? tmp$4 : Kotlin.throwNPE()).right)) {
                            var tmp$5;
                            p = (tmp$5 = (p != null ? p : Kotlin.throwNPE()).parent) != null ? tmp$5 : Kotlin.throwNPE();
                          }
                          return (p != null ? p : Kotlin.throwNPE()).parent;
                        }
                      }
                       else {
                        return null;
                      }
                    }
                  },
                  previous: function () {
                    var p = this;
                    if ((p != null ? p : Kotlin.throwNPE()).left != null) {
                      var tmp$0;
                      p = (tmp$0 = (p != null ? p : Kotlin.throwNPE()).left) != null ? tmp$0 : Kotlin.throwNPE();
                      while ((p != null ? p : Kotlin.throwNPE()).right != null) {
                        var tmp$1;
                        p = (tmp$1 = (p != null ? p : Kotlin.throwNPE()).right) != null ? tmp$1 : Kotlin.throwNPE();
                      }
                      return p;
                    }
                     else {
                      if ((p != null ? p : Kotlin.throwNPE()).parent != null) {
                        var tmp$2;
                        if (Kotlin.equals(p, ((tmp$2 = (p != null ? p : Kotlin.throwNPE()).parent) != null ? tmp$2 : Kotlin.throwNPE()).right)) {
                          var tmp$3;
                          return (tmp$3 = (p != null ? p : Kotlin.throwNPE()).parent) != null ? tmp$3 : Kotlin.throwNPE();
                        }
                         else {
                          var tmp$4;
                          while ((p != null ? p : Kotlin.throwNPE()).parent != null && Kotlin.equals(p, ((tmp$4 = (p != null ? p : Kotlin.throwNPE()).parent) != null ? tmp$4 : Kotlin.throwNPE()).left)) {
                            var tmp$5;
                            p = (tmp$5 = (p != null ? p : Kotlin.throwNPE()).parent) != null ? tmp$5 : Kotlin.throwNPE();
                          }
                          return (p != null ? p : Kotlin.throwNPE()).parent;
                        }
                      }
                       else {
                        return null;
                      }
                    }
                  }
                }),
                ReaderContext: Kotlin.createClass(null, function (payload, offset) {
                  this.payload = payload;
                  this.offset = offset;
                }, /** @lends _.org.kevoree.modeling.api.time.blob.ReaderContext.prototype */ {
                  unserialize: function (rightBranch) {
                    if (this.offset >= this.payload.length) {
                      return null;
                    }
                    var tokenBuild = new Kotlin.StringBuilder();
                    var ch = this.payload.charAt(this.offset);
                    if (ch === '%') {
                      if (rightBranch) {
                        this.offset = this.offset + 1;
                      }
                      return null;
                    }
                    if (ch === '#') {
                      this.offset = this.offset + 1;
                      return null;
                    }
                    if (ch !== '|') {
                      throw new Error('Error while loading BTree');
                    }
                    this.offset = this.offset + 1;
                    ch = this.payload.charAt(this.offset);
                    var color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                    var state = _.org.kevoree.modeling.api.time.blob.STATE.object.EXISTS;
                    {
                      if (ch === _.org.kevoree.modeling.api.time.blob.RBCONST.BLACK_DELETE) {
                        color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                        state = _.org.kevoree.modeling.api.time.blob.STATE.object.DELETED;
                      }
                       else if (ch === _.org.kevoree.modeling.api.time.blob.RBCONST.BLACK_EXISTS) {
                        color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                        state = _.org.kevoree.modeling.api.time.blob.STATE.object.EXISTS;
                      }
                       else if (ch === _.org.kevoree.modeling.api.time.blob.RBCONST.RED_DELETE) {
                        color = _.org.kevoree.modeling.api.time.blob.Color.object.RED;
                        state = _.org.kevoree.modeling.api.time.blob.STATE.object.DELETED;
                      }
                       else if (ch === _.org.kevoree.modeling.api.time.blob.RBCONST.RED_EXISTS) {
                        color = _.org.kevoree.modeling.api.time.blob.Color.object.RED;
                        state = _.org.kevoree.modeling.api.time.blob.STATE.object.EXISTS;
                      }
                    }
                    this.offset = this.offset + 1;
                    ch = this.payload.charAt(this.offset);
                    while (this.offset + 1 < this.payload.length && ch !== '|' && ch !== '#' && ch !== '%') {
                      tokenBuild.append(ch);
                      this.offset = this.offset + 1;
                      ch = this.payload.charAt(this.offset);
                    }
                    if (ch !== '|' && ch !== '#' && ch !== '%') {
                      tokenBuild.append(ch);
                    }
                    var p = new _.org.kevoree.modeling.api.time.blob.Node(_.java.lang.Long.parseLong(tokenBuild.toString()), state, color, null, null);
                    var left = this.unserialize(false);
                    if (left != null) {
                      left.parent = p;
                    }
                    var right = this.unserialize(true);
                    if (right != null) {
                      right.parent = p;
                    }
                    p.left = left;
                    p.right = right;
                    return p;
                  }
                }),
                RBTree: Kotlin.createClass(null, function () {
                  this.root = null;
                  this.size_pjslhb$ = 0;
                }, /** @lends _.org.kevoree.modeling.api.time.blob.RBTree.prototype */ {
                  size: function () {
                    return this.size_pjslhb$;
                  },
                  serialize: function () {
                    var builder = new Kotlin.StringBuilder();
                    builder.append(this.size_pjslhb$);
                    var tmp$0;
                    (tmp$0 = this.root) != null ? tmp$0.serialize(builder) : null;
                    return builder.toString();
                  },
                  unserialize: function (payload) {
                    if (_.kotlin.get_size_0(payload) === 0) {
                      return;
                    }
                    var i = 0;
                    var buffer = new Kotlin.StringBuilder();
                    var ch = payload.charAt(i);
                    while (i < payload.length && ch !== '|') {
                      buffer.append(ch);
                      i = i + 1;
                      ch = payload.charAt(i);
                    }
                    this.size_pjslhb$ = _.java.lang.Integer.parseInt(buffer.toString());
                    this.root = (new _.org.kevoree.modeling.api.time.blob.ReaderContext(payload, i)).unserialize(true);
                  },
                  previousOrEqual: function (key) {
                    var p = this.root;
                    if (p == null) {
                      return null;
                    }
                    while (p != null) {
                      if (key === (p != null ? p : Kotlin.throwNPE()).key) {
                        return p;
                      }
                      if (key > (p != null ? p : Kotlin.throwNPE()).key) {
                        if ((p != null ? p : Kotlin.throwNPE()).right != null) {
                          p = (p != null ? p : Kotlin.throwNPE()).right;
                        }
                         else {
                          return p;
                        }
                      }
                       else {
                        if ((p != null ? p : Kotlin.throwNPE()).left != null) {
                          p = (p != null ? p : Kotlin.throwNPE()).left;
                        }
                         else {
                          var parent = (p != null ? p : Kotlin.throwNPE()).parent;
                          var ch = p;
                          while (parent != null && Kotlin.equals(ch, (parent != null ? parent : Kotlin.throwNPE()).left)) {
                            ch = parent;
                            parent = (parent != null ? parent : Kotlin.throwNPE()).parent;
                          }
                          return parent;
                        }
                      }
                    }
                    return null;
                  },
                  nextOrEqual: function (key) {
                    var p = this.root;
                    if (p == null) {
                      return null;
                    }
                    while (p != null) {
                      if (key === (p != null ? p : Kotlin.throwNPE()).key) {
                        return p;
                      }
                      if (key < (p != null ? p : Kotlin.throwNPE()).key) {
                        if ((p != null ? p : Kotlin.throwNPE()).left != null) {
                          p = (p != null ? p : Kotlin.throwNPE()).left;
                        }
                         else {
                          return p;
                        }
                      }
                       else {
                        if ((p != null ? p : Kotlin.throwNPE()).right != null) {
                          p = (p != null ? p : Kotlin.throwNPE()).right;
                        }
                         else {
                          var parent = (p != null ? p : Kotlin.throwNPE()).parent;
                          var ch = p;
                          while (parent != null && Kotlin.equals(ch, (parent != null ? parent : Kotlin.throwNPE()).right)) {
                            ch = parent;
                            parent = (parent != null ? parent : Kotlin.throwNPE()).parent;
                          }
                          return parent;
                        }
                      }
                    }
                    return null;
                  },
                  previous: function (key) {
                    var p = this.root;
                    if (p == null) {
                      return null;
                    }
                    while (p != null) {
                      if (key < (p != null ? p : Kotlin.throwNPE()).key) {
                        if ((p != null ? p : Kotlin.throwNPE()).left != null) {
                          var tmp$0;
                          p = (tmp$0 = (p != null ? p : Kotlin.throwNPE()).left) != null ? tmp$0 : Kotlin.throwNPE();
                        }
                         else {
                          return (p != null ? p : Kotlin.throwNPE()).previous();
                        }
                      }
                       else if (key > (p != null ? p : Kotlin.throwNPE()).key) {
                        if ((p != null ? p : Kotlin.throwNPE()).right != null) {
                          var tmp$1;
                          p = (tmp$1 = (p != null ? p : Kotlin.throwNPE()).right) != null ? tmp$1 : Kotlin.throwNPE();
                        }
                         else {
                          return p;
                        }
                      }
                       else {
                        return (p != null ? p : Kotlin.throwNPE()).previous();
                      }
                    }
                    return null;
                  },
                  previousWhileNot: function (key, until) {
                    var elm = this.previousOrEqual(key);
                    if (Kotlin.equals((elm != null ? elm : Kotlin.throwNPE()).value, until)) {
                      return null;
                    }
                     else {
                      if ((elm != null ? elm : Kotlin.throwNPE()).key === key) {
                        elm = (elm != null ? elm : Kotlin.throwNPE()).previous();
                      }
                    }
                    if (elm == null || Kotlin.equals((elm != null ? elm : Kotlin.throwNPE()).value, until)) {
                      return null;
                    }
                     else {
                      return elm;
                    }
                  },
                  next: function (key) {
                    var p = this.root;
                    if (p == null) {
                      return null;
                    }
                    while (p != null) {
                      if (key < (p != null ? p : Kotlin.throwNPE()).key) {
                        if ((p != null ? p : Kotlin.throwNPE()).left != null) {
                          var tmp$0;
                          p = (tmp$0 = (p != null ? p : Kotlin.throwNPE()).left) != null ? tmp$0 : Kotlin.throwNPE();
                        }
                         else {
                          return p;
                        }
                      }
                       else if (key > (p != null ? p : Kotlin.throwNPE()).key) {
                        if ((p != null ? p : Kotlin.throwNPE()).right != null) {
                          var tmp$1;
                          p = (tmp$1 = (p != null ? p : Kotlin.throwNPE()).right) != null ? tmp$1 : Kotlin.throwNPE();
                        }
                         else {
                          return (p != null ? p : Kotlin.throwNPE()).next();
                        }
                      }
                       else {
                        return (p != null ? p : Kotlin.throwNPE()).next();
                      }
                    }
                    return null;
                  },
                  nextWhileNot: function (key, until) {
                    var elm = this.nextOrEqual(key);
                    if (Kotlin.equals((elm != null ? elm : Kotlin.throwNPE()).value, until)) {
                      return null;
                    }
                     else {
                      if ((elm != null ? elm : Kotlin.throwNPE()).key === key) {
                        elm = (elm != null ? elm : Kotlin.throwNPE()).next();
                      }
                    }
                    if (elm == null || Kotlin.equals((elm != null ? elm : Kotlin.throwNPE()).value, until)) {
                      return null;
                    }
                     else {
                      return elm;
                    }
                  },
                  first: function () {
                    var p = this.root;
                    if (p == null) {
                      return null;
                    }
                    while (p != null) {
                      if ((p != null ? p : Kotlin.throwNPE()).left != null) {
                        var tmp$0;
                        p = (tmp$0 = (p != null ? p : Kotlin.throwNPE()).left) != null ? tmp$0 : Kotlin.throwNPE();
                      }
                       else {
                        return p;
                      }
                    }
                    return null;
                  },
                  last: function () {
                    var p = this.root;
                    if (p == null) {
                      return null;
                    }
                    while (p != null) {
                      if ((p != null ? p : Kotlin.throwNPE()).right != null) {
                        var tmp$0;
                        p = (tmp$0 = (p != null ? p : Kotlin.throwNPE()).right) != null ? tmp$0 : Kotlin.throwNPE();
                      }
                       else {
                        return p;
                      }
                    }
                    return null;
                  },
                  firstWhileNot: function (key, until) {
                    var elm = this.previousOrEqual(key);
                    if (elm == null) {
                      return null;
                    }
                     else if (Kotlin.equals((elm != null ? elm : Kotlin.throwNPE()).value, until)) {
                      return null;
                    }
                    var prev;
                    do {
                      prev = (elm != null ? elm : Kotlin.throwNPE()).previous();
                      if (prev == null || Kotlin.equals((prev != null ? prev : Kotlin.throwNPE()).value, until)) {
                        return elm;
                      }
                       else {
                        elm = prev;
                      }
                    }
                     while (elm != null);
                    return prev;
                  },
                  lastWhileNot: function (key, until) {
                    var elm = this.previousOrEqual(key);
                    if (elm == null) {
                      return null;
                    }
                     else if (Kotlin.equals((elm != null ? elm : Kotlin.throwNPE()).value, until)) {
                      return null;
                    }
                    var next;
                    do {
                      next = (elm != null ? elm : Kotlin.throwNPE()).next();
                      if (next == null || Kotlin.equals((next != null ? next : Kotlin.throwNPE()).value, until)) {
                        return elm;
                      }
                       else {
                        elm = next;
                      }
                    }
                     while (elm != null);
                    return next;
                  },
                  lookupNode: function (key) {
                    var n = this.root;
                    if (n == null) {
                      return null;
                    }
                    while (n != null) {
                      if (key === (n != null ? n : Kotlin.throwNPE()).key) {
                        return n;
                      }
                       else {
                        if (key < (n != null ? n : Kotlin.throwNPE()).key) {
                          n = (n != null ? n : Kotlin.throwNPE()).left;
                        }
                         else {
                          n = (n != null ? n : Kotlin.throwNPE()).right;
                        }
                      }
                    }
                    return n;
                  },
                  lookup: function (key) {
                    var n = this.lookupNode(key);
                    if (n == null) {
                      return null;
                    }
                     else {
                      return n.value;
                    }
                  },
                  rotateLeft: function (n) {
                    var r = n.right;
                    this.replaceNode(n, r != null ? r : Kotlin.throwNPE());
                    n.right = r.left;
                    if (r.left != null) {
                      var tmp$0;
                      ((tmp$0 = r.left) != null ? tmp$0 : Kotlin.throwNPE()).parent = n;
                    }
                    r.left = n;
                    n.parent = r;
                  },
                  rotateRight: function (n) {
                    var l = n.left;
                    this.replaceNode(n, l != null ? l : Kotlin.throwNPE());
                    n.left = l.right;
                    if (l.right != null) {
                      var tmp$0;
                      ((tmp$0 = l.right) != null ? tmp$0 : Kotlin.throwNPE()).parent = n;
                    }
                    l.right = n;
                    n.parent = l;
                  },
                  replaceNode: function (oldn, newn) {
                    if (oldn.parent == null) {
                      this.root = newn;
                    }
                     else {
                      var tmp$0;
                      if (Kotlin.equals(oldn, ((tmp$0 = oldn.parent) != null ? tmp$0 : Kotlin.throwNPE()).left)) {
                        var tmp$1;
                        ((tmp$1 = oldn.parent) != null ? tmp$1 : Kotlin.throwNPE()).left = newn;
                      }
                       else {
                        var tmp$2;
                        ((tmp$2 = oldn.parent) != null ? tmp$2 : Kotlin.throwNPE()).right = newn;
                      }
                    }
                    if (newn != null) {
                      newn.parent = oldn.parent;
                    }
                  },
                  insert: function (key, value) {
                    var insertedNode = new _.org.kevoree.modeling.api.time.blob.Node(key, value, _.org.kevoree.modeling.api.time.blob.Color.object.RED, null, null);
                    if (this.root == null) {
                      this.size_pjslhb$++;
                      this.root = insertedNode;
                    }
                     else {
                      var n = this.root;
                      while (true) {
                        if (key === (n != null ? n : Kotlin.throwNPE()).key) {
                          (n != null ? n : Kotlin.throwNPE()).value = value;
                          return;
                        }
                         else if (key < (n != null ? n : Kotlin.throwNPE()).key) {
                          if ((n != null ? n : Kotlin.throwNPE()).left == null) {
                            (n != null ? n : Kotlin.throwNPE()).left = insertedNode;
                            this.size_pjslhb$++;
                            break;
                          }
                           else {
                            var tmp$0;
                            n = (tmp$0 = (n != null ? n : Kotlin.throwNPE()).left) != null ? tmp$0 : Kotlin.throwNPE();
                          }
                        }
                         else {
                          if ((n != null ? n : Kotlin.throwNPE()).right == null) {
                            (n != null ? n : Kotlin.throwNPE()).right = insertedNode;
                            this.size_pjslhb$++;
                            break;
                          }
                           else {
                            n = (n != null ? n : Kotlin.throwNPE()).right;
                          }
                        }
                      }
                      insertedNode.parent = n;
                    }
                    this.insertCase1(insertedNode);
                  },
                  insertCase1: function (n) {
                    if (n.parent == null) {
                      n.color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                    }
                     else {
                      this.insertCase2(n);
                    }
                  },
                  insertCase2: function (n) {
                    if (Kotlin.equals(this.nodeColor(n.parent), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK)) {
                      return;
                    }
                     else {
                      this.insertCase3(n);
                    }
                  },
                  insertCase3: function (n) {
                    if (Kotlin.equals(this.nodeColor(n.uncle()), _.org.kevoree.modeling.api.time.blob.Color.object.RED)) {
                      var tmp$0, tmp$1, tmp$2, tmp$3;
                      ((tmp$0 = n.parent) != null ? tmp$0 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                      ((tmp$1 = n.uncle()) != null ? tmp$1 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                      ((tmp$2 = n.grandparent()) != null ? tmp$2 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.RED;
                      this.insertCase1((tmp$3 = n.grandparent()) != null ? tmp$3 : Kotlin.throwNPE());
                    }
                     else {
                      this.insertCase4(n);
                    }
                  },
                  insertCase4: function (n_n) {
                    var n = n_n;
                    var tmp$0, tmp$1;
                    if (Kotlin.equals(n, ((tmp$0 = n.parent) != null ? tmp$0 : Kotlin.throwNPE()).right) && Kotlin.equals(n.parent, ((tmp$1 = n.grandparent()) != null ? tmp$1 : Kotlin.throwNPE()).left)) {
                      var tmp$2, tmp$3;
                      this.rotateLeft((tmp$2 = n.parent) != null ? tmp$2 : Kotlin.throwNPE());
                      n = (tmp$3 = n.left) != null ? tmp$3 : Kotlin.throwNPE();
                    }
                     else {
                      var tmp$4, tmp$5;
                      if (Kotlin.equals(n, ((tmp$4 = n.parent) != null ? tmp$4 : Kotlin.throwNPE()).left) && Kotlin.equals(n.parent, ((tmp$5 = n.grandparent()) != null ? tmp$5 : Kotlin.throwNPE()).right)) {
                        var tmp$6, tmp$7;
                        this.rotateRight((tmp$6 = n.parent) != null ? tmp$6 : Kotlin.throwNPE());
                        n = (tmp$7 = n.right) != null ? tmp$7 : Kotlin.throwNPE();
                      }
                    }
                    this.insertCase5(n);
                  },
                  insertCase5: function (n) {
                    var tmp$0, tmp$1, tmp$2, tmp$3;
                    ((tmp$0 = n.parent) != null ? tmp$0 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                    ((tmp$1 = n.grandparent()) != null ? tmp$1 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.RED;
                    if (Kotlin.equals(n, ((tmp$2 = n.parent) != null ? tmp$2 : Kotlin.throwNPE()).left) && Kotlin.equals(n.parent, ((tmp$3 = n.grandparent()) != null ? tmp$3 : Kotlin.throwNPE()).left)) {
                      var tmp$4;
                      this.rotateRight((tmp$4 = n.grandparent()) != null ? tmp$4 : Kotlin.throwNPE());
                    }
                     else {
                      var tmp$5;
                      this.rotateLeft((tmp$5 = n.grandparent()) != null ? tmp$5 : Kotlin.throwNPE());
                    }
                  },
                  delete: function (key) {
                    var n = this.lookupNode(key);
                    if (n == null) {
                      return;
                    }
                     else {
                      this.size_pjslhb$--;
                      if ((n != null ? n : Kotlin.throwNPE()).left != null && (n != null ? n : Kotlin.throwNPE()).right != null) {
                        var tmp$0;
                        var pred = (tmp$0 = (n != null ? n : Kotlin.throwNPE()).left) != null ? tmp$0 : Kotlin.throwNPE();
                        while (pred.right != null) {
                          var tmp$1;
                          pred = (tmp$1 = pred.right) != null ? tmp$1 : Kotlin.throwNPE();
                        }
                        (n != null ? n : Kotlin.throwNPE()).key = pred.key;
                        (n != null ? n : Kotlin.throwNPE()).value = pred.value;
                        n = pred;
                      }
                      var tmp$2;
                      if ((n != null ? n : Kotlin.throwNPE()).right == null) {
                        tmp$2 = (n != null ? n : Kotlin.throwNPE()).left;
                      }
                       else {
                        tmp$2 = (n != null ? n : Kotlin.throwNPE()).right;
                      }
                      var child = tmp$2;
                      if (Kotlin.equals(this.nodeColor(n), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK)) {
                        (n != null ? n : Kotlin.throwNPE()).color = this.nodeColor(child);
                        this.deleteCase1(n != null ? n : Kotlin.throwNPE());
                      }
                      this.replaceNode(n != null ? n : Kotlin.throwNPE(), child);
                    }
                  },
                  deleteCase1: function (n) {
                    if (n.parent == null) {
                      return;
                    }
                     else {
                      this.deleteCase2(n);
                    }
                  },
                  deleteCase2: function (n) {
                    if (Kotlin.equals(this.nodeColor(n.sibling()), _.org.kevoree.modeling.api.time.blob.Color.object.RED)) {
                      var tmp$0, tmp$1, tmp$2;
                      ((tmp$0 = n.parent) != null ? tmp$0 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.RED;
                      ((tmp$1 = n.sibling()) != null ? tmp$1 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                      if (Kotlin.equals(n, ((tmp$2 = n.parent) != null ? tmp$2 : Kotlin.throwNPE()).left)) {
                        var tmp$3;
                        this.rotateLeft((tmp$3 = n.parent) != null ? tmp$3 : Kotlin.throwNPE());
                      }
                       else {
                        var tmp$4;
                        this.rotateRight((tmp$4 = n.parent) != null ? tmp$4 : Kotlin.throwNPE());
                      }
                    }
                    this.deleteCase3(n);
                  },
                  deleteCase3: function (n) {
                    var tmp$0, tmp$1;
                    if (Kotlin.equals(this.nodeColor(n.parent), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK) && Kotlin.equals(this.nodeColor(n.sibling()), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK) && Kotlin.equals(this.nodeColor(((tmp$0 = n.sibling()) != null ? tmp$0 : Kotlin.throwNPE()).left), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK) && Kotlin.equals(this.nodeColor(((tmp$1 = n.sibling()) != null ? tmp$1 : Kotlin.throwNPE()).right), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK)) {
                      var tmp$2, tmp$3;
                      ((tmp$2 = n.sibling()) != null ? tmp$2 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.RED;
                      this.deleteCase1((tmp$3 = n.parent) != null ? tmp$3 : Kotlin.throwNPE());
                    }
                     else {
                      this.deleteCase4(n);
                    }
                  },
                  deleteCase4: function (n) {
                    var tmp$0, tmp$1;
                    if (Kotlin.equals(this.nodeColor(n.parent), _.org.kevoree.modeling.api.time.blob.Color.object.RED) && Kotlin.equals(this.nodeColor(n.sibling()), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK) && Kotlin.equals(this.nodeColor(((tmp$0 = n.sibling()) != null ? tmp$0 : Kotlin.throwNPE()).left), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK) && Kotlin.equals(this.nodeColor(((tmp$1 = n.sibling()) != null ? tmp$1 : Kotlin.throwNPE()).right), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK)) {
                      var tmp$2, tmp$3;
                      ((tmp$2 = n.sibling()) != null ? tmp$2 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.RED;
                      ((tmp$3 = n.parent) != null ? tmp$3 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                    }
                     else {
                      this.deleteCase5(n);
                    }
                  },
                  deleteCase5: function (n) {
                    var tmp$0, tmp$1, tmp$2, tmp$7, tmp$8, tmp$9;
                    if (Kotlin.equals(n, ((tmp$0 = n.parent) != null ? tmp$0 : Kotlin.throwNPE()).left) && Kotlin.equals(this.nodeColor(n.sibling()), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK) && Kotlin.equals(this.nodeColor(((tmp$1 = n.sibling()) != null ? tmp$1 : Kotlin.throwNPE()).left), _.org.kevoree.modeling.api.time.blob.Color.object.RED) && Kotlin.equals(this.nodeColor(((tmp$2 = n.sibling()) != null ? tmp$2 : Kotlin.throwNPE()).right), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK)) {
                      var tmp$3, tmp$4, tmp$5, tmp$6;
                      ((tmp$3 = n.sibling()) != null ? tmp$3 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.RED;
                      ((tmp$5 = ((tmp$4 = n.sibling()) != null ? tmp$4 : Kotlin.throwNPE()).left) != null ? tmp$5 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                      this.rotateRight((tmp$6 = n.sibling()) != null ? tmp$6 : Kotlin.throwNPE());
                    }
                     else if (Kotlin.equals(n, ((tmp$7 = n.parent) != null ? tmp$7 : Kotlin.throwNPE()).right) && Kotlin.equals(this.nodeColor(n.sibling()), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK) && Kotlin.equals(this.nodeColor(((tmp$8 = n.sibling()) != null ? tmp$8 : Kotlin.throwNPE()).right), _.org.kevoree.modeling.api.time.blob.Color.object.RED) && Kotlin.equals(this.nodeColor(((tmp$9 = n.sibling()) != null ? tmp$9 : Kotlin.throwNPE()).left), _.org.kevoree.modeling.api.time.blob.Color.object.BLACK)) {
                      var tmp$10, tmp$11, tmp$12, tmp$13;
                      ((tmp$10 = n.sibling()) != null ? tmp$10 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.RED;
                      ((tmp$12 = ((tmp$11 = n.sibling()) != null ? tmp$11 : Kotlin.throwNPE()).right) != null ? tmp$12 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                      this.rotateLeft((tmp$13 = n.sibling()) != null ? tmp$13 : Kotlin.throwNPE());
                    }
                    this.deleteCase6(n);
                  },
                  deleteCase6: function (n) {
                    var tmp$0, tmp$1, tmp$2;
                    ((tmp$0 = n.sibling()) != null ? tmp$0 : Kotlin.throwNPE()).color = this.nodeColor(n.parent);
                    ((tmp$1 = n.parent) != null ? tmp$1 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                    if (Kotlin.equals(n, ((tmp$2 = n.parent) != null ? tmp$2 : Kotlin.throwNPE()).left)) {
                      var tmp$3, tmp$4, tmp$5;
                      ((tmp$4 = ((tmp$3 = n.sibling()) != null ? tmp$3 : Kotlin.throwNPE()).right) != null ? tmp$4 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                      this.rotateLeft((tmp$5 = n.parent) != null ? tmp$5 : Kotlin.throwNPE());
                    }
                     else {
                      var tmp$6, tmp$7, tmp$8;
                      ((tmp$7 = ((tmp$6 = n.sibling()) != null ? tmp$6 : Kotlin.throwNPE()).left) != null ? tmp$7 : Kotlin.throwNPE()).color = _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                      this.rotateRight((tmp$8 = n.parent) != null ? tmp$8 : Kotlin.throwNPE());
                    }
                  },
                  nodeColor: function (n) {
                    if (n == null) {
                      return _.org.kevoree.modeling.api.time.blob.Color.object.BLACK;
                    }
                     else {
                      return n.color;
                    }
                  }
                })
              })
            }),
            xmi: Kotlin.definePackage(function () {
              this.Token = Kotlin.createObject(null, function () {
                this.XML_HEADER = 0;
                this.END_DOCUMENT = 1;
                this.START_TAG = 2;
                this.END_TAG = 3;
                this.COMMENT = 4;
                this.SINGLETON_TAG = 5;
              });
            }, /** @lends _.org.kevoree.modeling.api.xmi */ {
              ReferencesVisitor: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.util.ModelVisitor];
              }, function $fun(ostream, addressTable, elementsCount, resourceSet) {
                $fun.baseInitializer.call(this);
                this.ostream = ostream;
                this.addressTable = addressTable;
                this.elementsCount = elementsCount;
                this.resourceSet = resourceSet;
                this.value = null;
              }, /** @lends _.org.kevoree.modeling.api.xmi.ReferencesVisitor.prototype */ {
                endVisitRef: function (refName) {
                  if (this.value != null) {
                    this.ostream.print_4(' ' + refName + '="' + Kotlin.toString(this.value) + '"');
                    this.value = null;
                  }
                },
                visit: function (elem, refNameInParent, parent) {
                  var tmp$0;
                  var adjustedAddress = (tmp$0 = this.resourceSet) != null ? tmp$0.objToAddr(elem) : null;
                  if (adjustedAddress == null) {
                    adjustedAddress = this.addressTable.get_za3rmp$(elem);
                  }
                  if (this.value == null) {
                    this.value = adjustedAddress;
                  }
                   else {
                    var tmp$1, tmp$2;
                    this.value = ((tmp$1 = this.value) != null ? tmp$1 : Kotlin.throwNPE()) + ' ';
                    this.value = ((tmp$2 = this.value) != null ? tmp$2 : Kotlin.throwNPE()) + Kotlin.toString(adjustedAddress);
                  }
                }
              }),
              AttributesVisitor: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.util.ModelAttributeVisitor];
              }, function (ostream, ignoreGeneratedID) {
                this.ostream = ostream;
                this.ignoreGeneratedID = ignoreGeneratedID;
              }, /** @lends _.org.kevoree.modeling.api.xmi.AttributesVisitor.prototype */ {
                visit: function (value, name, parent) {
                  if (value != null) {
                    if (this.ignoreGeneratedID && Kotlin.equals(name, 'generated_KMF_ID')) {
                      return;
                    }
                    if (typeof value === 'string' && Kotlin.equals(value, '')) {
                      return;
                    }
                    this.ostream.print_4(' ' + name + '="');
                    if (Kotlin.isType(value, Date)) {
                      this.escapeXml(this.ostream, '' + value.getTime());
                    }
                     else {
                      this.escapeXml(this.ostream, _.org.kevoree.modeling.api.util.AttConverter.convFlatAtt(value));
                    }
                    this.ostream.print_4('"');
                  }
                },
                escapeXml: function (ostream, chain) {
                  if (chain == null) {
                    return;
                  }
                  var i = 0;
                  var max = chain.length;
                  while (i < max) {
                    var c = chain.charAt(i);
                    if (c === '"') {
                      ostream.print_4('&quot;');
                    }
                     else if (c === '&') {
                      ostream.print_4('&amp;');
                    }
                     else if (c === "'") {
                      ostream.print_4('&apos;');
                    }
                     else if (c === '<') {
                      ostream.print_4('&lt;');
                    }
                     else if (c === '>') {
                      ostream.print_4('&gt;');
                    }
                     else {
                      ostream.print_1(c);
                    }
                    i = i + 1;
                  }
                }
              }),
              ModelSerializationVisitor: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.util.ModelVisitor];
              }, function $fun(ostream, addressTable, elementsCount, resourceSet, ignoreGeneratedID) {
                $fun.baseInitializer.call(this);
                this.ostream = ostream;
                this.addressTable = addressTable;
                this.elementsCount = elementsCount;
                this.resourceSet = resourceSet;
                this.attributeVisitor = new _.org.kevoree.modeling.api.xmi.AttributesVisitor(this.ostream, ignoreGeneratedID);
                this.referenceVisitor = new _.org.kevoree.modeling.api.xmi.ReferencesVisitor(this.ostream, this.addressTable, this.elementsCount, this.resourceSet);
              }, /** @lends _.org.kevoree.modeling.api.xmi.ModelSerializationVisitor.prototype */ {
                visit: function (elem, refNameInParent, parent) {
                  this.ostream.print_1('<');
                  this.ostream.print_4(refNameInParent);
                  this.ostream.print_4(' xsi:type="' + this.formatMetaClassName(elem.metaClassName()) + '"');
                  elem.visitAttributes(this.attributeVisitor);
                  elem.visit(this.referenceVisitor, false, false, true);
                  this.ostream.println_1('>');
                  elem.visit(this, false, true, false);
                  this.ostream.print_4('<\/');
                  this.ostream.print_4(refNameInParent);
                  this.ostream.print_1('>');
                  this.ostream.println();
                },
                formatMetaClassName: function (metaClassName) {
                  var lastPoint = _.js.lastIndexOf_960177$(metaClassName, '.');
                  var pack = metaClassName.substring(0, lastPoint);
                  var cls = metaClassName.substring(lastPoint + 1);
                  return pack + ':' + cls;
                }
              }),
              ModelAddressVisitor: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.util.ModelVisitor];
              }, function $fun(addressTable, elementsCount, packageList) {
                $fun.baseInitializer.call(this);
                this.addressTable = addressTable;
                this.elementsCount = elementsCount;
                this.packageList = packageList;
              }, /** @lends _.org.kevoree.modeling.api.xmi.ModelAddressVisitor.prototype */ {
                visit: function (elem, refNameInParent, parent) {
                  var tmp$0, tmp$1;
                  var parentXmiAddress = (tmp$0 = this.addressTable.get_za3rmp$(parent)) != null ? tmp$0 : Kotlin.throwNPE();
                  var i = (tmp$1 = this.elementsCount.get_za3rmp$(parentXmiAddress + '/@' + refNameInParent)) != null ? tmp$1 : 0;
                  this.addressTable.put_wn2jw4$(elem, parentXmiAddress + '/@' + refNameInParent + '.' + i);
                  this.elementsCount.put_wn2jw4$(parentXmiAddress + '/@' + refNameInParent, i + 1);
                  var pack = elem.metaClassName().substring(0, _.js.lastIndexOf_960177$(elem.metaClassName(), '.'));
                  if (!this.packageList.contains_za3rmp$(pack))
                    this.packageList.add_za3rmp$(pack);
                }
              }),
              XMIModelSerializer: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.ModelSerializer];
              }, function () {
                this.resourceSet = null;
                this.ignoreGeneratedID = false;
              }, /** @lends _.org.kevoree.modeling.api.xmi.XMIModelSerializer.prototype */ {
                serialize: function (oMS) {
                  var oo = new _.java.io.ByteArrayOutputStream();
                  this.serializeToStream(oMS, oo);
                  oo.flush();
                  return oo.toString();
                },
                serializeToStream: function (oMS, ostream) {
                  var wt = new _.java.io.PrintStream(new _.java.io.BufferedOutputStream(ostream), false);
                  var addressTable = new Kotlin.ComplexHashMap();
                  var packageList = new Kotlin.ArrayList();
                  addressTable.put_wn2jw4$(oMS, '/');
                  var elementsCount = new Kotlin.PrimitiveHashMap();
                  var addressBuilderVisitor = new _.org.kevoree.modeling.api.xmi.ModelAddressVisitor(addressTable, elementsCount, packageList);
                  oMS.visit(addressBuilderVisitor, true, true, false);
                  var masterVisitor = new _.org.kevoree.modeling.api.xmi.ModelSerializationVisitor(wt, addressTable, elementsCount, this.resourceSet, this.ignoreGeneratedID);
                  wt.println_2('<?xml version="1.0" encoding="UTF-8"?>');
                  wt.print_4('<' + this.formatMetaClassName(oMS.metaClassName()).replace('.', '_'));
                  wt.print_4(' xmlns:xsi="http://wwww.w3.org/2001/XMLSchema-instance"');
                  wt.print_4(' xmi:version="2.0"');
                  wt.print_4(' xmlns:xmi="http://www.omg.org/XMI"');
                  var index = 0;
                  while (index < _.kotlin.get_size_1(packageList)) {
                    wt.print_4(' xmlns:' + packageList.get_za3lpa$(index).replace('.', '_') + '="http://' + packageList.get_za3lpa$(index) + '"');
                    index++;
                  }
                  oMS.visitAttributes(new _.org.kevoree.modeling.api.xmi.AttributesVisitor(wt, this.ignoreGeneratedID));
                  oMS.visit(new _.org.kevoree.modeling.api.xmi.ReferencesVisitor(wt, addressTable, elementsCount, this.resourceSet), false, false, true);
                  wt.println_2('>');
                  oMS.visit(masterVisitor, false, true, false);
                  wt.println_2('<\/' + this.formatMetaClassName(oMS.metaClassName()).replace('.', '_') + '>');
                  wt.flush();
                },
                formatMetaClassName: function (metaClassName) {
                  var lastPoint = _.js.lastIndexOf_960177$(metaClassName, '.');
                  var pack = metaClassName.substring(0, lastPoint);
                  var cls = metaClassName.substring(lastPoint + 1);
                  return pack + ':' + cls;
                }
              }),
              XMIModelLoader: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.ModelLoader];
              }, function (factory) {
                this.factory = factory;
                this.resourceSet = null;
                this.LOADER_XMI_LOCAL_NAME = 'type';
                this.LOADER_XMI_XSI = 'xsi';
                this.LOADER_XMI_NS_URI = 'nsURI';
                this.attributesHashmap_7wijs5$ = new Kotlin.PrimitiveHashMap();
                this.referencesHashmap_cc1kom$ = new Kotlin.PrimitiveHashMap();
                this.namedElementSupportActivated_71goxr$ = false;
                this.attributeVisitor_g67dla$ = _.org.kevoree.modeling.api.xmi.XMIModelLoader.XMIModelLoader$f(this);
                this.referencesVisitor_g5fzti$ = _.org.kevoree.modeling.api.xmi.XMIModelLoader.XMIModelLoader$f_0(this);
              }, /** @lends _.org.kevoree.modeling.api.xmi.XMIModelLoader.prototype */ {
                activateSupportForNamedElements: function (activate) {
                  this.namedElementSupportActivated_71goxr$ = activate;
                },
                unescapeXml: function (src) {
                  var builder = null;
                  var i = 0;
                  while (i < src.length) {
                    var c = src.charAt(i);
                    if (c === '&') {
                      if (builder == null) {
                        builder = new Kotlin.StringBuilder();
                        (builder != null ? builder : Kotlin.throwNPE()).append(src.substring(0, i));
                      }
                      if (src.charAt(i + 1) === 'a') {
                        if (src.charAt(i + 2) === 'm') {
                          builder != null ? builder.append('&') : null;
                          i = i + 5;
                        }
                         else if (src.charAt(i + 2) === 'p') {
                          builder != null ? builder.append("'") : null;
                          i = i + 6;
                        }
                         else {
                          Kotlin.println('Could not unescaped chain:' + src.charAt(i) + src.charAt(i + 1) + src.charAt(i + 2));
                        }
                      }
                       else if (src.charAt(i + 1) === 'q') {
                        builder != null ? builder.append('"') : null;
                        i = i + 6;
                      }
                       else if (src.charAt(i + 1) === 'l') {
                        builder != null ? builder.append('<') : null;
                        i = i + 4;
                      }
                       else if (src.charAt(i + 1) === 'g') {
                        builder != null ? builder.append('>') : null;
                        i = i + 4;
                      }
                       else {
                        Kotlin.println('Could not unescaped chain:' + src.charAt(i) + src.charAt(i + 1));
                      }
                    }
                     else {
                      if (builder != null) {
                        builder != null ? builder.append(c) : null;
                      }
                      i++;
                    }
                  }
                  if (builder != null) {
                    return Kotlin.toString(builder);
                  }
                   else {
                    return src;
                  }
                },
                loadModelFromString: function (str) {
                  var reader = new _.org.kevoree.modeling.api.xmi.XmlParser(_.org.kevoree.modeling.api.util.ByteConverter.byteArrayInputStreamFromString(str));
                  if (reader.hasNext()) {
                    return this.deserialize(reader);
                  }
                   else {
                    Kotlin.println('Loader::Nothing in the String !');
                    return null;
                  }
                },
                loadModelFromStream: function (inputStream) {
                  var reader = new _.org.kevoree.modeling.api.xmi.XmlParser(inputStream);
                  if (reader.hasNext()) {
                    return this.deserialize(reader);
                  }
                   else {
                    Kotlin.println('Loader::Nothing in the file !');
                    return null;
                  }
                },
                loadObject: function (ctx, xmiAddress, objectType) {
                  if (objectType === void 0)
                    objectType = null;
                  var tmp$0, tmp$12, tmp$13, tmp$14, tmp$15, tmp$16, tmp$17, tmp$18;
                  var elementTagName = ((tmp$0 = ctx.xmiReader) != null ? tmp$0 : Kotlin.throwNPE()).getLocalName();
                  var modelElem;
                  if (objectType != null) {
                    var tmp$1;
                    modelElem = (tmp$1 = this.factory) != null ? tmp$1.create(objectType) : null;
                    if (modelElem == null) {
                      var xsiType = null;
                      var tmp$2, tmp$3, tmp$4, tmp$5, tmp$6;
                      {
                        tmp$3 = new Kotlin.NumberRange(0, ((tmp$2 = ctx.xmiReader) != null ? tmp$2 : Kotlin.throwNPE()).getAttributeCount() - 1), tmp$4 = tmp$3.start, tmp$5 = tmp$3.end, tmp$6 = tmp$3.increment;
                        for (var i = tmp$4; i <= tmp$5; i += tmp$6) {
                          var tmp$7, tmp$8;
                          var localName = ((tmp$7 = ctx.xmiReader) != null ? tmp$7 : Kotlin.throwNPE()).getAttributeLocalName(i);
                          var xsi = ((tmp$8 = ctx.xmiReader) != null ? tmp$8 : Kotlin.throwNPE()).getAttributePrefix(i);
                          if (Kotlin.equals(localName, this.LOADER_XMI_LOCAL_NAME) && Kotlin.equals(xsi, this.LOADER_XMI_XSI)) {
                            var tmp$9;
                            xsiType = ((tmp$9 = ctx.xmiReader) != null ? tmp$9 : Kotlin.throwNPE()).getAttributeValue(i);
                            break;
                          }
                        }
                      }
                      if (xsiType != null) {
                        var realTypeName = xsiType != null ? xsiType.substring(0, (xsiType != null ? xsiType : Kotlin.throwNPE()).lastIndexOf(':')) : null;
                        var realName = (xsiType != null ? xsiType : Kotlin.throwNPE()).substring((xsiType != null ? xsiType : Kotlin.throwNPE()).lastIndexOf(':') + 1, (xsiType != null ? xsiType : Kotlin.throwNPE()).length);
                        var tmp$10;
                        modelElem = (tmp$10 = this.factory) != null ? tmp$10.create(_.kotlin.plus_n7iowf$(realTypeName, '.') + realName) : null;
                      }
                    }
                  }
                   else {
                    var tmp$11;
                    modelElem = (tmp$11 = this.factory) != null ? tmp$11.create(elementTagName) : null;
                  }
                  if (modelElem == null) {
                    Kotlin.println('Could not create an object for local name ' + elementTagName);
                  }
                  ctx.map.put_wn2jw4$(xmiAddress, modelElem != null ? modelElem : Kotlin.throwNPE());
                  if (!this.attributesHashmap_7wijs5$.containsKey_za3rmp$((modelElem != null ? modelElem : Kotlin.throwNPE()).metaClassName())) {
                    modelElem != null ? modelElem.visitAttributes(this.attributeVisitor_g67dla$) : null;
                  }
                  var elemAttributesMap = (tmp$12 = this.attributesHashmap_7wijs5$.get_za3rmp$((modelElem != null ? modelElem : Kotlin.throwNPE()).metaClassName())) != null ? tmp$12 : Kotlin.throwNPE();
                  if (!this.referencesHashmap_cc1kom$.containsKey_za3rmp$((modelElem != null ? modelElem : Kotlin.throwNPE()).metaClassName())) {
                    modelElem != null ? modelElem.visit(this.referencesVisitor_g5fzti$, false, true, false) : null;
                  }
                  var elemReferencesMap = (tmp$13 = this.referencesHashmap_cc1kom$.get_za3rmp$((modelElem != null ? modelElem : Kotlin.throwNPE()).metaClassName())) != null ? tmp$13 : Kotlin.throwNPE();
                  {
                    tmp$15 = new Kotlin.NumberRange(0, ((tmp$14 = ctx.xmiReader) != null ? tmp$14 : Kotlin.throwNPE()).getAttributeCount() - 1), tmp$16 = tmp$15.start, tmp$17 = tmp$15.end, tmp$18 = tmp$15.increment;
                    for (var i_0 = tmp$16; i_0 <= tmp$17; i_0 += tmp$18) {
                      var tmp$19;
                      var prefix = ((tmp$19 = ctx.xmiReader) != null ? tmp$19 : Kotlin.throwNPE()).getAttributePrefix(i_0);
                      if (prefix == null || Kotlin.equals(prefix, '')) {
                        var tmp$20, tmp$21;
                        var attrName = ((tmp$20 = ctx.xmiReader) != null ? tmp$20 : Kotlin.throwNPE()).getAttributeLocalName(i_0).trim();
                        var valueAtt = ((tmp$21 = ctx.xmiReader) != null ? tmp$21 : Kotlin.throwNPE()).getAttributeValue(i_0).trim();
                        if (valueAtt != null) {
                          if (elemAttributesMap.containsKey_za3rmp$(attrName)) {
                            modelElem != null ? modelElem.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, attrName, this.unescapeXml(valueAtt), false, false) : null;
                            if (this.namedElementSupportActivated_71goxr$ && Kotlin.equals(attrName, 'name')) {
                              var parent = ctx.map.get_za3rmp$(xmiAddress.substring(0, xmiAddress.lastIndexOf('/')));
                              {
                                var tmp$22 = _.kotlin.toList_h3panj$(ctx.map.entrySet()).iterator();
                                while (tmp$22.hasNext()) {
                                  var entry = tmp$22.next();
                                  if (Kotlin.equals(_.kotlin.get_value(entry), parent)) {
                                    var refT = _.kotlin.get_key(entry) + '/' + this.unescapeXml(valueAtt);
                                    ctx.map.put_wn2jw4$(refT, modelElem != null ? modelElem : Kotlin.throwNPE());
                                  }
                                }
                              }
                            }
                          }
                           else {
                            if (!valueAtt.startsWith('#') && !valueAtt.startsWith('/')) {
                              if (this.resourceSet != null) {
                                var tmp$23;
                                var previousLoadedRef = ((tmp$23 = this.resourceSet) != null ? tmp$23 : Kotlin.throwNPE()).resolveObject(valueAtt);
                                if (previousLoadedRef != null) {
                                  modelElem != null ? modelElem.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, attrName, previousLoadedRef, true, false) : null;
                                }
                                 else {
                                  throw new Error('Unresolve NsURI based XMI reference ' + valueAtt);
                                }
                              }
                               else {
                                throw new Error('Bad XMI reference ' + valueAtt);
                              }
                            }
                             else {
                              var tmp$24, tmp$25, tmp$26;
                              {
                                tmp$24 = Kotlin.splitString(valueAtt, ' '), tmp$25 = tmp$24.length;
                                for (var tmp$26 = 0; tmp$26 !== tmp$25; ++tmp$26) {
                                  var xmiRef = tmp$24[tmp$26];
                                  var tmp$27, tmp$28;
                                  if (xmiRef.startsWith('#')) {
                                    tmp$27 = xmiRef.substring(1);
                                  }
                                   else {
                                    tmp$27 = xmiRef;
                                  }
                                  var adjustedRef = tmp$27;
                                  if (adjustedRef.startsWith('//')) {
                                    tmp$28 = '/0' + adjustedRef.substring(1);
                                  }
                                   else {
                                    tmp$28 = adjustedRef;
                                  }
                                  adjustedRef = tmp$28;
                                  adjustedRef = adjustedRef.replace('.0', '');
                                  var ref = ctx.map.get_za3rmp$(adjustedRef);
                                  if (ref != null) {
                                    modelElem != null ? modelElem.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, attrName, ref, true, false) : null;
                                  }
                                   else {
                                    ctx.resolvers.add_za3rmp$(new _.org.kevoree.modeling.api.xmi.XMIResolveCommand(ctx, modelElem != null ? modelElem : Kotlin.throwNPE(), _.org.kevoree.modeling.api.util.ActionType.object.ADD, attrName, adjustedRef, this.resourceSet));
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                  var done = false;
                  while (!done) {
                    var tmp$29;
                    {
                      var tmp$30 = ((tmp$29 = ctx.xmiReader) != null ? tmp$29 : Kotlin.throwNPE()).next();
                      if (tmp$30 === _.org.kevoree.modeling.api.xmi.Token.START_TAG) {
                        var tmp$31, tmp$32, tmp$34;
                        var subElemName = ((tmp$31 = ctx.xmiReader) != null ? tmp$31 : Kotlin.throwNPE()).getLocalName();
                        var i_1 = (tmp$32 = ctx.elementsCount.get_za3rmp$(xmiAddress + '/@' + subElemName)) != null ? tmp$32 : 0;
                        var tmp$33 = xmiAddress + '/@' + subElemName;
                        if (i_1 !== 0) {
                          tmp$34 = '.' + i_1;
                        }
                         else {
                          tmp$34 = '';
                        }
                        var subElementId = tmp$33 + tmp$34;
                        var containedElement = this.loadObject(ctx, subElementId, elemReferencesMap.get_za3rmp$(subElemName));
                        modelElem != null ? modelElem.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, subElemName, containedElement, true, false) : null;
                        ctx.elementsCount.put_wn2jw4$(xmiAddress + '/@' + subElemName, i_1 + 1);
                      }
                       else if (tmp$30 === _.org.kevoree.modeling.api.xmi.Token.END_TAG) {
                        var tmp$35;
                        if (Kotlin.equals(((tmp$35 = ctx.xmiReader) != null ? tmp$35 : Kotlin.throwNPE()).getLocalName(), elementTagName)) {
                          done = true;
                        }
                      }
                       else {
                      }
                    }
                  }
                  return modelElem != null ? modelElem : Kotlin.throwNPE();
                },
                deserialize: function (reader) {
                  var nsURI = null;
                  var context = new _.org.kevoree.modeling.api.xmi.LoadingContext();
                  context.xmiReader = reader;
                  while (reader.hasNext()) {
                    var nextTag = reader.next();
                    {
                      if (nextTag === _.org.kevoree.modeling.api.xmi.Token.START_TAG) {
                        var localName = reader.getLocalName();
                        if (localName != null) {
                          var loadedRootsSize = context.loadedRoots.size();
                          var ns = new Kotlin.PrimitiveHashMap();
                          var tmp$0, tmp$1, tmp$2, tmp$3, tmp$4;
                          {
                            tmp$1 = new Kotlin.NumberRange(0, ((tmp$0 = context.xmiReader) != null ? tmp$0 : Kotlin.throwNPE()).getAttributeCount() - 1), tmp$2 = tmp$1.start, tmp$3 = tmp$1.end, tmp$4 = tmp$1.increment;
                            for (var i = tmp$2; i <= tmp$3; i += tmp$4) {
                              var tmp$5, tmp$6;
                              var localName_0 = ((tmp$5 = context.xmiReader) != null ? tmp$5 : Kotlin.throwNPE()).getAttributeLocalName(i);
                              var localValue = ((tmp$6 = context.xmiReader) != null ? tmp$6 : Kotlin.throwNPE()).getAttributeValue(i);
                              if (Kotlin.equals(localName_0, this.LOADER_XMI_NS_URI)) {
                                nsURI = localValue;
                              }
                              ns.put_wn2jw4$(localName_0, localValue);
                            }
                          }
                          var xsiType = reader.tagPrefix;
                          var realTypeName = ns.get_za3rmp$(xsiType);
                          if (realTypeName == null) {
                            realTypeName = xsiType;
                          }
                          context.loadedRoots.add_za3rmp$(this.loadObject(context, '/' + loadedRootsSize, Kotlin.toString(xsiType) + '.' + localName));
                        }
                         else {
                          Kotlin.println('Tried to read a tag with null tag_name.');
                        }
                      }
                       else if (nextTag === _.org.kevoree.modeling.api.xmi.Token.END_TAG) {
                        break;
                      }
                       else if (nextTag === _.org.kevoree.modeling.api.xmi.Token.END_DOCUMENT) {
                        break;
                      }
                       else {
                      }
                    }
                  }
                  {
                    var tmp$7 = context.resolvers.iterator();
                    while (tmp$7.hasNext()) {
                      var res = tmp$7.next();
                      res.run();
                    }
                  }
                  if (this.resourceSet != null && nsURI != null) {
                    var tmp$8;
                    ((tmp$8 = this.resourceSet) != null ? tmp$8 : Kotlin.throwNPE()).registerXmiAddrMappedObjects(nsURI != null ? nsURI : Kotlin.throwNPE(), context.map);
                  }
                  return context.loadedRoots;
                }
              }, /** @lends _.org.kevoree.modeling.api.xmi.XMIModelLoader */ {
                visit$f: function () {
                  return new Kotlin.PrimitiveHashMap();
                },
                XMIModelLoader$f: function (this$XMIModelLoader) {
                  return Kotlin.createObject(function () {
                    return [_.org.kevoree.modeling.api.util.ModelAttributeVisitor];
                  }, null, {
                    visit: function (value, name, parent) {
                      _.kotlin.getOrPut_ynyybx$(this$XMIModelLoader.attributesHashmap_7wijs5$, parent.metaClassName(), _.org.kevoree.modeling.api.xmi.XMIModelLoader.visit$f).put_wn2jw4$(name, true);
                    }
                  });
                },
                beginVisitElem$f: function () {
                  return new Kotlin.PrimitiveHashMap();
                },
                XMIModelLoader$f_0: function (this$XMIModelLoader) {
                  return Kotlin.createObject(function () {
                    return [_.org.kevoree.modeling.api.util.ModelVisitor];
                  }, function $fun() {
                    $fun.baseInitializer.call(this);
                    this.refMap = null;
                  }, {
                    beginVisitElem: function (elem) {
                      this.refMap = _.kotlin.getOrPut_ynyybx$(this$XMIModelLoader.referencesHashmap_cc1kom$, elem.metaClassName(), _.org.kevoree.modeling.api.xmi.XMIModelLoader.beginVisitElem$f);
                    },
                    endVisitElem: function (elem) {
                      this.refMap = null;
                    },
                    beginVisitRef: function (refName, refType) {
                      var tmp$0;
                      ((tmp$0 = this.refMap) != null ? tmp$0 : Kotlin.throwNPE()).put_wn2jw4$(refName, refType);
                      return true;
                    },
                    visit: function (elem, refNameInParent, parent) {
                    }
                  });
                }
              }),
              LoadingContext: Kotlin.createClass(null, function () {
                this.xmiReader = null;
                this.loadedRoots = new Kotlin.ArrayList();
                this.map = new Kotlin.PrimitiveHashMap();
                this.elementsCount = new Kotlin.PrimitiveHashMap();
                this.resolvers = new Kotlin.ArrayList();
                this.stats = new Kotlin.PrimitiveHashMap();
                this.oppositesAlreadySet = new Kotlin.PrimitiveHashMap();
              }, /** @lends _.org.kevoree.modeling.api.xmi.LoadingContext.prototype */ {
                isOppositeAlreadySet: function (localRef, oppositeRef) {
                  var res = this.oppositesAlreadySet.get_za3rmp$(oppositeRef + '_' + localRef) != null || this.oppositesAlreadySet.get_za3rmp$(localRef + '_' + oppositeRef) != null;
                  return res;
                },
                storeOppositeRelation: function (localRef, oppositeRef) {
                  this.oppositesAlreadySet.put_wn2jw4$(localRef + '_' + oppositeRef, true);
                }
              }),
              XMIResolveCommand: Kotlin.createClass(null, function (context, target, mutatorType, refName, ref, resourceSet) {
                this.context = context;
                this.target = target;
                this.mutatorType = mutatorType;
                this.refName = refName;
                this.ref = ref;
                this.resourceSet = resourceSet;
              }, /** @lends _.org.kevoree.modeling.api.xmi.XMIResolveCommand.prototype */ {
                run: function () {
                  var referencedElement = this.context.map.get_za3rmp$(this.ref);
                  if (referencedElement != null) {
                    this.target.reflexiveMutator(this.mutatorType, this.refName, referencedElement, true, false);
                    return;
                  }
                  if (Kotlin.equals(this.ref, '/0/') || Kotlin.equals(this.ref, '/')) {
                    referencedElement = this.context.map.get_za3rmp$('/0');
                    if (referencedElement != null) {
                      this.target.reflexiveMutator(this.mutatorType, this.refName, referencedElement, true, false);
                      return;
                    }
                  }
                  if (this.resourceSet != null) {
                    referencedElement = this.resourceSet.resolveObject(this.ref);
                    if (referencedElement != null) {
                      this.target.reflexiveMutator(this.mutatorType, this.refName, referencedElement, true, false);
                      return;
                    }
                  }
                  throw new Error('KMF Load error : reference ' + this.ref + ' not found in map when trying to  ' + this.mutatorType + ' ' + this.refName + '  on ' + this.target.metaClassName() + '(path:' + this.target.path() + ')');
                }
              }),
              XmlParser: Kotlin.createClass(null, function (inputStream) {
                this.inputStream = inputStream;
                this.bytes_gdnk4p$ = this.inputStream.readBytes();
                this.index_gharkg$ = -1;
                this.currentChar_x9b225$ = null;
                this.xmlVersion_ywy43n$ = null;
                this.xmlCharset_tph6x5$ = null;
                this.tagName_b61wcj$ = '';
                this.tagPrefix = null;
                this.attributesNames_b5o00h$ = new Kotlin.ArrayList();
                this.attributesPrefixes_hgbl8n$ = new Kotlin.ArrayList();
                this.attributesValues_d28x97$ = new Kotlin.ArrayList();
                this.attributeName_f9qnph$ = new Kotlin.StringBuilder();
                this.attributePrefix_r6drlg$ = null;
                this.attributeValue_npfmfd$ = new Kotlin.StringBuilder();
                this.readSingleton_h1okvh$ = false;
              }, /** @lends _.org.kevoree.modeling.api.xmi.XmlParser.prototype */ {
                hasNext: function () {
                  return this.bytes_gdnk4p$.length - this.index_gharkg$ > 2;
                },
                getLocalName: function () {
                  return this.tagName_b61wcj$;
                },
                getAttributeCount: function () {
                  return this.attributesNames_b5o00h$.size();
                },
                getAttributeLocalName: function (i) {
                  return this.attributesNames_b5o00h$.get_za3lpa$(i);
                },
                getAttributePrefix: function (i) {
                  return this.attributesPrefixes_hgbl8n$.get_za3lpa$(i);
                },
                getAttributeValue: function (i) {
                  return this.attributesValues_d28x97$.get_za3lpa$(i);
                },
                readChar: function () {
                  return _.org.kevoree.modeling.api.util.ByteConverter.toChar(this.bytes_gdnk4p$[++this.index_gharkg$]);
                },
                next: function () {
                  if (this.readSingleton_h1okvh$) {
                    this.readSingleton_h1okvh$ = false;
                    return _.org.kevoree.modeling.api.xmi.Token.END_TAG;
                  }
                  if (!this.hasNext()) {
                    return _.org.kevoree.modeling.api.xmi.Token.END_DOCUMENT;
                  }
                  this.attributesNames_b5o00h$.clear();
                  this.attributesPrefixes_hgbl8n$.clear();
                  this.attributesValues_d28x97$.clear();
                  this.read_lessThan();
                  this.currentChar_x9b225$ = this.readChar();
                  if (this.currentChar_x9b225$ === '?') {
                    this.currentChar_x9b225$ = this.readChar();
                    this.read_xmlHeader();
                    return _.org.kevoree.modeling.api.xmi.Token.XML_HEADER;
                  }
                   else if (this.currentChar_x9b225$ === '!') {
                    do {
                      this.currentChar_x9b225$ = this.readChar();
                    }
                     while (this.currentChar_x9b225$ !== '>');
                    return _.org.kevoree.modeling.api.xmi.Token.COMMENT;
                  }
                   else if (this.currentChar_x9b225$ === '/') {
                    this.currentChar_x9b225$ = this.readChar();
                    this.read_closingTag();
                    return _.org.kevoree.modeling.api.xmi.Token.END_TAG;
                  }
                   else {
                    this.read_openTag();
                    if (this.currentChar_x9b225$ === '/') {
                      this.read_upperThan();
                      this.readSingleton_h1okvh$ = true;
                    }
                    return _.org.kevoree.modeling.api.xmi.Token.START_TAG;
                  }
                },
                read_lessThan: function () {
                  do {
                    this.currentChar_x9b225$ = this.readChar();
                  }
                   while (this.currentChar_x9b225$ !== '<');
                },
                read_upperThan: function () {
                  while (this.currentChar_x9b225$ !== '>') {
                    this.currentChar_x9b225$ = this.readChar();
                  }
                },
                read_xmlHeader: function () {
                  this.read_tagName();
                  this.read_attributes();
                  this.read_upperThan();
                },
                read_closingTag: function () {
                  this.read_tagName();
                  this.read_upperThan();
                },
                read_openTag: function () {
                  this.read_tagName();
                  if (this.currentChar_x9b225$ !== '>' && this.currentChar_x9b225$ !== '/') {
                    this.read_attributes();
                  }
                },
                read_tagName: function () {
                  this.tagName_b61wcj$ = '' + this.currentChar_x9b225$;
                  this.tagPrefix = null;
                  this.currentChar_x9b225$ = this.readChar();
                  while (this.currentChar_x9b225$ !== ' ' && this.currentChar_x9b225$ !== '>' && this.currentChar_x9b225$ !== '/') {
                    if (this.currentChar_x9b225$ === ':') {
                      this.tagPrefix = this.tagName_b61wcj$;
                      this.tagName_b61wcj$ = '';
                    }
                     else {
                      this.tagName_b61wcj$ += this.currentChar_x9b225$;
                    }
                    this.currentChar_x9b225$ = this.readChar();
                  }
                },
                read_attributes: function () {
                  var end_of_tag = false;
                  while (this.currentChar_x9b225$ === ' ') {
                    this.currentChar_x9b225$ = this.readChar();
                  }
                  while (!end_of_tag) {
                    while (this.currentChar_x9b225$ !== '=') {
                      if (this.currentChar_x9b225$ === ':') {
                        this.attributePrefix_r6drlg$ = this.attributeName_f9qnph$.toString();
                        this.attributeName_f9qnph$ = new Kotlin.StringBuilder();
                      }
                       else {
                        var tmp$0;
                        this.attributeName_f9qnph$.append((tmp$0 = this.currentChar_x9b225$) != null ? tmp$0 : Kotlin.throwNPE());
                      }
                      this.currentChar_x9b225$ = this.readChar();
                    }
                    do {
                      this.currentChar_x9b225$ = this.readChar();
                    }
                     while (this.currentChar_x9b225$ !== '"');
                    this.currentChar_x9b225$ = this.readChar();
                    while (this.currentChar_x9b225$ !== '"') {
                      var tmp$1;
                      this.attributeValue_npfmfd$.append((tmp$1 = this.currentChar_x9b225$) != null ? tmp$1 : Kotlin.throwNPE());
                      this.currentChar_x9b225$ = this.readChar();
                    }
                    this.attributesNames_b5o00h$.add_za3rmp$(this.attributeName_f9qnph$.toString());
                    this.attributesPrefixes_hgbl8n$.add_za3rmp$(this.attributePrefix_r6drlg$);
                    this.attributesValues_d28x97$.add_za3rmp$(this.attributeValue_npfmfd$.toString());
                    this.attributeName_f9qnph$ = new Kotlin.StringBuilder();
                    this.attributePrefix_r6drlg$ = null;
                    this.attributeValue_npfmfd$ = new Kotlin.StringBuilder();
                    do {
                      this.currentChar_x9b225$ = this.readChar();
                      if (this.currentChar_x9b225$ === '?' || this.currentChar_x9b225$ === '/' || this.currentChar_x9b225$ === '-' || this.currentChar_x9b225$ === '>') {
                        end_of_tag = true;
                      }
                    }
                     while (!end_of_tag && this.currentChar_x9b225$ === ' ');
                  }
                }
              }),
              ResourceSet: Kotlin.createClass(null, function () {
                this.resources_twji9r$ = new Kotlin.PrimitiveHashMap();
                this.invertedResources_583d58$ = new Kotlin.ComplexHashMap();
              }, /** @lends _.org.kevoree.modeling.api.xmi.ResourceSet.prototype */ {
                registerXmiAddrMappedObjects: function (nsuri, xmiAddrs) {
                  this.resources_twji9r$.put_wn2jw4$(nsuri, xmiAddrs);
                  {
                    var tmp$0 = _.kotlin.iterator_s8ckw1$(xmiAddrs);
                    while (tmp$0.hasNext()) {
                      var ad = tmp$0.next();
                      if (this.invertedResources_583d58$.containsKey_za3rmp$(_.kotlin.get_value(ad))) {
                        var alreadyVal = this.invertedResources_583d58$.get_za3rmp$(_.kotlin.get_value(ad));
                        if ((alreadyVal != null ? alreadyVal : Kotlin.throwNPE()).addr.contains('@')) {
                          this.invertedResources_583d58$.put_wn2jw4$(_.kotlin.get_value(ad), new _.org.kevoree.modeling.api.xmi.XmiObjAddr(nsuri, _.kotlin.get_key(ad)));
                        }
                      }
                       else {
                        this.invertedResources_583d58$.put_wn2jw4$(_.kotlin.get_value(ad), new _.org.kevoree.modeling.api.xmi.XmiObjAddr(nsuri, _.kotlin.get_key(ad)));
                      }
                    }
                  }
                },
                resolveObject: function (xmiAddr) {
                  var typeAndAddr = Kotlin.splitString(xmiAddr, ' ');
                  if (typeAndAddr.length > 1) {
                    var addrs = Kotlin.splitString(typeAndAddr[1], '#');
                    if (addrs.length === 2) {
                      var resolvedAddrs = this.resources_twji9r$.get_za3rmp$(addrs[0]);
                      var addr = addrs[1];
                      addr = '#' + addr;
                      addr = addr.replace('#//', '/0/');
                      return resolvedAddrs != null ? resolvedAddrs.get_za3rmp$(addr) : null;
                    }
                  }
                  return null;
                },
                objToAddr: function (obj) {
                  var resolved = this.invertedResources_583d58$.get_za3rmp$(obj);
                  if (resolved != null) {
                    var packName = this.formatMetaClassName(obj.metaClassName());
                    var nsURI = resolved.nsuri;
                    var addr = resolved.addr;
                    addr = addr.replace('/0/', '#//');
                    return packName + ' ' + nsURI + addr;
                  }
                  return null;
                },
                formatMetaClassName: function (metaClassName) {
                  var lastPoint = _.js.lastIndexOf_960177$(metaClassName, '.');
                  var pack = metaClassName.substring(0, lastPoint);
                  var cls = metaClassName.substring(lastPoint + 1);
                  return pack + ':' + cls;
                }
              }),
              XmiObjAddr: Kotlin.createClass(null, function (nsuri, addr) {
                this.nsuri = nsuri;
                this.addr = addr;
              }, /** @lends _.org.kevoree.modeling.api.xmi.XmiObjAddr.prototype */ {
                component1: function () {
                  return this.nsuri;
                },
                component2: function () {
                  return this.addr;
                },
                copy: function (nsuri, addr) {
                  return new _.org.kevoree.modeling.api.xmi.XmiObjAddr(nsuri === void 0 ? this.nsuri : nsuri, addr === void 0 ? this.addr : addr);
                },
                toString: function () {
                  return 'XmiObjAddr(nsuri=' + Kotlin.toString(this.nsuri) + (', addr=' + Kotlin.toString(this.addr)) + ')';
                },
                hashCode: function () {
                  var result = 977237871;
                  result = result * 31 + Kotlin.hashCode(this.nsuri) | 0;
                  result = result * 31 + Kotlin.hashCode(this.addr) | 0;
                  return result;
                },
                equals_za3rmp$: function (other) {
                  return this === other || (other !== null && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.nsuri, other.nsuri) && Kotlin.equals(this.addr, other.addr))));
                }
              })
            }),
            trace: Kotlin.definePackage(function () {
              this.ModelTraceConstants = Kotlin.createObject(null, function () {
                this.traceType = 't';
                this.src = 's';
                this.refname = 'r';
                this.previouspath = 'p';
                this.typename = 'n';
                this.objpath = 'o';
                this.content = 'c';
                this.openJSON = '{';
                this.closeJSON = '}';
                this.bb = '"';
                this.coma = ',';
                this.dp = ':';
              });
            }, /** @lends _.org.kevoree.modeling.api.trace */ {
              Event2Trace: Kotlin.createClass(null, function (compare) {
                this.compare = compare;
              }, /** @lends _.org.kevoree.modeling.api.trace.Event2Trace.prototype */ {
                convert: function (event) {
                  var result = new Kotlin.ArrayList();
                  {
                    var tmp$0 = event.etype;
                    if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                      var tmp$1;
                      result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelRemoveTrace((tmp$1 = event.previousPath) != null ? tmp$1 : Kotlin.throwNPE(), event.elementAttributeName, Kotlin.toString(event.previous_value)));
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL) {
                      var tmp$2;
                      result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelRemoveAllTrace((tmp$2 = event.previousPath) != null ? tmp$2 : Kotlin.throwNPE(), event.elementAttributeName));
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                      var tmp$3, tmp$4;
                      var casted = (tmp$3 = event.value) != null ? tmp$3 : Kotlin.throwNPE();
                      var traces = this.compare.inter(casted, casted);
                      result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace((tmp$4 = event.previousPath) != null ? tmp$4 : Kotlin.throwNPE(), event.elementAttributeName, casted.path(), casted.metaClassName()));
                      result.addAll_xeylzf$(traces.traces);
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL) {
                      var tmp$5;
                      var casted_0 = (tmp$5 = event.value) != null ? tmp$5 : Kotlin.throwNPE();
                      {
                        var tmp$6 = casted_0.iterator();
                        while (tmp$6.hasNext()) {
                          var elem = tmp$6.next();
                          var elemCasted = elem != null ? elem : Kotlin.throwNPE();
                          var traces_0 = this.compare.inter(elemCasted, elemCasted);
                          var tmp$7;
                          result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace((tmp$7 = event.previousPath) != null ? tmp$7 : Kotlin.throwNPE(), event.elementAttributeName, elemCasted.path(), elemCasted.metaClassName()));
                          result.addAll_xeylzf$(traces_0.traces);
                        }
                      }
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.SET) {
                      if (Kotlin.equals(event.elementAttributeType, _.org.kevoree.modeling.api.util.ElementAttributeType.object.ATTRIBUTE)) {
                        var tmp$8;
                        result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelSetTrace((tmp$8 = event.previousPath) != null ? tmp$8 : Kotlin.throwNPE(), event.elementAttributeName, null, _.org.kevoree.modeling.api.util.AttConverter.convFlatAtt(event.value), null));
                      }
                       else {
                        var tmp$9, tmp$10;
                        result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelSetTrace((tmp$9 = event.previousPath) != null ? tmp$9 : Kotlin.throwNPE(), event.elementAttributeName, (tmp$10 = event.value) != null ? tmp$10.path() : null, null, null));
                      }
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                    }
                     else {
                      throw new Error("Can't convert event : " + event);
                    }
                  }
                  return (new _.org.kevoree.modeling.api.trace.TraceSequence(this.compare.factory)).populate(result);
                },
                inverse: function (event) {
                  var result = new Kotlin.ArrayList();
                  {
                    var tmp$0 = event.etype;
                    if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE) {
                      var tmp$1, tmp$2, tmp$3;
                      result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace((tmp$1 = event.previousPath) != null ? tmp$1 : Kotlin.throwNPE(), event.elementAttributeName, ((tmp$2 = event.value) != null ? tmp$2 : Kotlin.throwNPE()).path(), ((tmp$3 = event.value) != null ? tmp$3 : Kotlin.throwNPE()).metaClassName()));
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL) {
                      var tmp$4;
                      var casted = (tmp$4 = event.value) != null ? tmp$4 : Kotlin.throwNPE();
                      {
                        var tmp$5 = casted.iterator();
                        while (tmp$5.hasNext()) {
                          var elem = tmp$5.next();
                          var elemCasted = elem != null ? elem : Kotlin.throwNPE();
                          var traces = this.compare.inter(elemCasted, elemCasted);
                          var tmp$6;
                          result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace((tmp$6 = event.previousPath) != null ? tmp$6 : Kotlin.throwNPE(), event.elementAttributeName, elemCasted.path(), elemCasted.metaClassName()));
                          result.addAll_xeylzf$(traces.traces);
                        }
                      }
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.ADD) {
                      var tmp$7, tmp$8;
                      var casted_0 = (tmp$7 = event.value) != null ? tmp$7 : Kotlin.throwNPE();
                      var traces_0 = this.compare.inter(casted_0, casted_0);
                      result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelRemoveTrace((tmp$8 = event.previousPath) != null ? tmp$8 : Kotlin.throwNPE(), event.elementAttributeName, casted_0.path()));
                      result.addAll_xeylzf$(traces_0.traces);
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL) {
                      var tmp$9;
                      var casted_1 = (tmp$9 = event.value) != null ? tmp$9 : Kotlin.throwNPE();
                      {
                        var tmp$10 = casted_1.iterator();
                        while (tmp$10.hasNext()) {
                          var elem_0 = tmp$10.next();
                          var elemCasted_0 = elem_0 != null ? elem_0 : Kotlin.throwNPE();
                          var traces_1 = this.compare.inter(elemCasted_0, elemCasted_0);
                          var tmp$11;
                          result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelRemoveTrace((tmp$11 = event.previousPath) != null ? tmp$11 : Kotlin.throwNPE(), event.elementAttributeName, elemCasted_0.path()));
                          result.addAll_xeylzf$(traces_1.traces);
                        }
                      }
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.SET) {
                      if (Kotlin.equals(event.elementAttributeType, _.org.kevoree.modeling.api.util.ElementAttributeType.object.ATTRIBUTE)) {
                        var tmp$12;
                        result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelSetTrace((tmp$12 = event.previousPath) != null ? tmp$12 : Kotlin.throwNPE(), event.elementAttributeName, null, _.org.kevoree.modeling.api.util.AttConverter.convFlatAtt(event.previous_value), null));
                      }
                       else {
                        var tmp$13, tmp$14;
                        result.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelSetTrace((tmp$13 = event.previousPath) != null ? tmp$13 : Kotlin.throwNPE(), event.elementAttributeName, (tmp$14 = event.previous_value) != null ? tmp$14.path() : null, null, null));
                      }
                    }
                     else if (tmp$0 === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX) {
                    }
                     else {
                      throw new Error("Can't convert event : " + event);
                    }
                  }
                  return (new _.org.kevoree.modeling.api.trace.TraceSequence(this.compare.factory)).populate(result);
                }
              }),
              ModelTraceApplicator: Kotlin.createClass(null, function (targetModel, factory) {
                this.targetModel = targetModel;
                this.factory = factory;
                this.pendingObj = null;
                this.pendingParent = null;
                this.pendingParentRefName = null;
                this.pendingObjPath = null;
                this.fireEvents = true;
              }, /** @lends _.org.kevoree.modeling.api.trace.ModelTraceApplicator.prototype */ {
                tryClosePending: function (srcPath) {
                  if (this.pendingObj != null && !Kotlin.equals(this.pendingObjPath, srcPath)) {
                    var tmp$0, tmp$1;
                    ((tmp$0 = this.pendingParent) != null ? tmp$0 : Kotlin.throwNPE()).reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, (tmp$1 = this.pendingParentRefName) != null ? tmp$1 : Kotlin.throwNPE(), this.pendingObj, true, this.fireEvents);
                    this.pendingObj = null;
                    this.pendingObjPath = null;
                    this.pendingParentRefName = null;
                    this.pendingParent = null;
                  }
                },
                createOrAdd: function (previousPath, target, refName, potentialTypeName) {
                  var targetElem = null;
                  if (previousPath != null) {
                    targetElem = this.targetModel.findByPath(previousPath);
                  }
                  if (targetElem != null) {
                    target.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.ADD, refName, targetElem, true, this.fireEvents);
                  }
                   else {
                    if (potentialTypeName == null) {
                      throw new Error('Unknow typeName for potential path ' + Kotlin.toString(previousPath) + ', to store in ' + refName + ', unconsistency error');
                    }
                    this.pendingObj = this.factory.create(potentialTypeName);
                    this.pendingObjPath = previousPath;
                    this.pendingParentRefName = refName;
                    this.pendingParent = target;
                  }
                },
                applyTraceOnModel: function (traceSeq) {
                  {
                    var tmp$0 = traceSeq.traces.iterator();
                    while (tmp$0.hasNext()) {
                      var trace = tmp$0.next();
                      var target = this.targetModel;
                      if (Kotlin.isType(trace, _.org.kevoree.modeling.api.trace.ModelAddTrace)) {
                        this.tryClosePending(null);
                        if (!Kotlin.equals(trace.srcPath, '')) {
                          var resolvedTarget = this.targetModel.findByPath(trace.srcPath);
                          if (resolvedTarget == null) {
                            throw new Error('Add Trace source not found for path : ' + trace.srcPath + ' pending ' + this.pendingObjPath + '\n' + trace.toString());
                          }
                          target = resolvedTarget != null ? resolvedTarget : Kotlin.throwNPE();
                        }
                        this.createOrAdd(trace.previousPath, target, trace.refName, trace.typeName);
                      }
                      if (Kotlin.isType(trace, _.org.kevoree.modeling.api.trace.ModelAddAllTrace)) {
                        this.tryClosePending(null);
                        var i = 0;
                        var tmp$1;
                        {
                          var tmp$2 = ((tmp$1 = trace.previousPath) != null ? tmp$1 : Kotlin.throwNPE()).iterator();
                          while (tmp$2.hasNext()) {
                            var path = tmp$2.next();
                            var tmp$3;
                            this.createOrAdd(path, target, trace.refName, ((tmp$3 = trace.typeName) != null ? tmp$3 : Kotlin.throwNPE()).get_za3lpa$(i));
                            i++;
                          }
                        }
                      }
                      if (Kotlin.isType(trace, _.org.kevoree.modeling.api.trace.ModelRemoveTrace)) {
                        this.tryClosePending(trace.srcPath);
                        var tempTarget = this.targetModel;
                        if (!Kotlin.equals(trace.srcPath, '')) {
                          tempTarget = this.targetModel.findByPath(trace.srcPath);
                        }
                        if (tempTarget != null) {
                          (tempTarget != null ? tempTarget : Kotlin.throwNPE()).reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE, trace.refName, this.targetModel.findByPath(trace.objPath), true, this.fireEvents);
                        }
                      }
                      if (Kotlin.isType(trace, _.org.kevoree.modeling.api.trace.ModelRemoveAllTrace)) {
                        this.tryClosePending(trace.srcPath);
                        var tempTarget_0 = this.targetModel;
                        if (!Kotlin.equals(trace.srcPath, '')) {
                          tempTarget_0 = this.targetModel.findByPath(trace.srcPath);
                        }
                        if (tempTarget_0 != null) {
                          (tempTarget_0 != null ? tempTarget_0 : Kotlin.throwNPE()).reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL, trace.refName, null, true, this.fireEvents);
                        }
                      }
                      if (Kotlin.isType(trace, _.org.kevoree.modeling.api.trace.ModelSetTrace)) {
                        this.tryClosePending(trace.srcPath);
                        if (!Kotlin.equals(trace.srcPath, '') && !Kotlin.equals(trace.srcPath, this.pendingObjPath)) {
                          var tempObject = this.targetModel.findByPath(trace.srcPath);
                          if (tempObject == null) {
                            throw new Error('Set Trace source not found for path : ' + trace.srcPath + ' pending ' + this.pendingObjPath + '\n' + trace.toString());
                          }
                          target = tempObject != null ? tempObject : Kotlin.throwNPE();
                        }
                         else {
                          if (Kotlin.equals(trace.srcPath, this.pendingObjPath) && this.pendingObj != null) {
                            var tmp$4;
                            target = (tmp$4 = this.pendingObj) != null ? tmp$4 : Kotlin.throwNPE();
                          }
                        }
                        if (trace.content != null) {
                          target.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.SET, trace.refName, trace.content, true, this.fireEvents);
                        }
                         else {
                          var tmp$5;
                          if (trace.objPath != null) {
                            tmp$5 = this.targetModel.findByPath(trace.objPath);
                          }
                           else {
                            tmp$5 = null;
                          }
                          var targetContentPath = tmp$5;
                          if (targetContentPath != null) {
                            target.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.SET, trace.refName, targetContentPath, true, this.fireEvents);
                          }
                           else {
                            if (trace.typeName != null && !Kotlin.equals(trace.typeName, '')) {
                              this.createOrAdd(trace.objPath, target, trace.refName, trace.typeName);
                            }
                             else {
                              target.reflexiveMutator(_.org.kevoree.modeling.api.util.ActionType.object.SET, trace.refName, targetContentPath, true, this.fireEvents);
                            }
                          }
                        }
                      }
                    }
                  }
                  this.tryClosePending(null);
                }
              }),
              TraceSequence: Kotlin.createClass(null, function (factory) {
                this.factory = factory;
                this.traces = new Kotlin.ArrayList();
              }, /** @lends _.org.kevoree.modeling.api.trace.TraceSequence.prototype */ {
                populate: function (addtraces) {
                  this.traces.addAll_xeylzf$(addtraces);
                  return this;
                },
                append: function (seq) {
                  this.traces.addAll_xeylzf$(seq.traces);
                },
                populateFromString: function (addtracesTxt) {
                  return this.populateFromStream(_.org.kevoree.modeling.api.util.ByteConverter.byteArrayInputStreamFromString(addtracesTxt));
                },
                populateFromStream: function (inputStream) {
                  var previousControlSrc = null;
                  var previousControlTypeName = null;
                  var lexer = new _.org.kevoree.modeling.api.json.Lexer(inputStream);
                  var currentToken = lexer.nextToken();
                  if (currentToken.tokenType !== _.org.kevoree.modeling.api.json.Type.LEFT_BRACKET) {
                    throw new Error('Bad Format : expect [');
                  }
                  currentToken = lexer.nextToken();
                  var keys = new Kotlin.PrimitiveHashMap();
                  var previousName = null;
                  while (currentToken.tokenType !== _.org.kevoree.modeling.api.json.Type.EOF && currentToken.tokenType !== _.org.kevoree.modeling.api.json.Type.RIGHT_BRACKET) {
                    if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.LEFT_BRACE) {
                      keys.clear();
                    }
                    if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.VALUE) {
                      if (previousName != null) {
                        keys.put_wn2jw4$(previousName != null ? previousName : Kotlin.throwNPE(), Kotlin.toString(currentToken.value));
                        previousName = null;
                      }
                       else {
                        previousName = Kotlin.toString(currentToken.value);
                      }
                    }
                    if (currentToken.tokenType === _.org.kevoree.modeling.api.json.Type.RIGHT_BRACE) {
                      var traceTypeRead = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.traceType);
                      if (traceTypeRead == null) {
                        traceTypeRead = previousControlTypeName;
                      }
                      {
                        if (traceTypeRead === _.org.kevoree.modeling.api.util.ActionType.object.CONTROL.code) {
                          var src = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                          if (src != null) {
                            var tmp$0;
                            previousControlSrc = (tmp$0 = _.org.kevoree.modeling.api.json.JSONString.unescape(src)) != null ? tmp$0 : Kotlin.throwNPE();
                          }
                          var globalTypeName = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname);
                          if (globalTypeName != null) {
                            previousControlTypeName = globalTypeName;
                          }
                        }
                         else if (traceTypeRead === _.org.kevoree.modeling.api.util.ActionType.object.SET.code) {
                          var srcFound = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                          if (srcFound == null) {
                            srcFound = previousControlSrc;
                          }
                           else {
                            srcFound = _.org.kevoree.modeling.api.json.JSONString.unescape(srcFound);
                          }
                          var tmp$1;
                          this.traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelSetTrace(srcFound != null ? srcFound : Kotlin.throwNPE(), (tmp$1 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname)) != null ? tmp$1 : Kotlin.throwNPE(), _.org.kevoree.modeling.api.json.JSONString.unescape(keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.objpath)), _.org.kevoree.modeling.api.json.JSONString.unescape(keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.content)), _.org.kevoree.modeling.api.json.JSONString.unescape(keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.typename))));
                        }
                         else if (traceTypeRead === _.org.kevoree.modeling.api.util.ActionType.object.ADD.code) {
                          var srcFound_0 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                          if (srcFound_0 == null) {
                            srcFound_0 = previousControlSrc;
                          }
                           else {
                            srcFound_0 = _.org.kevoree.modeling.api.json.JSONString.unescape(srcFound_0);
                          }
                          var tmp$2, tmp$3;
                          this.traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddTrace(srcFound_0 != null ? srcFound_0 : Kotlin.throwNPE(), (tmp$2 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname)) != null ? tmp$2 : Kotlin.throwNPE(), _.org.kevoree.modeling.api.json.JSONString.unescape((tmp$3 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.previouspath)) != null ? tmp$3 : Kotlin.throwNPE()), keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.typename)));
                        }
                         else if (traceTypeRead === _.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL.code) {
                          var srcFound_1 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                          if (srcFound_1 == null) {
                            srcFound_1 = previousControlSrc;
                          }
                           else {
                            srcFound_1 = _.org.kevoree.modeling.api.json.JSONString.unescape(srcFound_1);
                          }
                          var tmp$4, tmp$5, tmp$6, tmp$7, tmp$8;
                          this.traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelAddAllTrace(srcFound_1 != null ? srcFound_1 : Kotlin.throwNPE(), (tmp$4 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname)) != null ? tmp$4 : Kotlin.throwNPE(), (tmp$6 = (tmp$5 = _.org.kevoree.modeling.api.json.JSONString.unescape(keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.content))) != null ? Kotlin.splitString(tmp$5, ';') : null) != null ? _.kotlin.toList_2hx8bi$(tmp$6) : null, (tmp$8 = (tmp$7 = _.org.kevoree.modeling.api.json.JSONString.unescape(keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.typename))) != null ? Kotlin.splitString(tmp$7, ';') : null) != null ? _.kotlin.toList_2hx8bi$(tmp$8) : null));
                        }
                         else if (traceTypeRead === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE.code) {
                          var srcFound_2 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                          if (srcFound_2 == null) {
                            srcFound_2 = previousControlSrc;
                          }
                           else {
                            srcFound_2 = _.org.kevoree.modeling.api.json.JSONString.unescape(srcFound_2);
                          }
                          var tmp$9, tmp$10, tmp$11;
                          this.traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelRemoveTrace(srcFound_2 != null ? srcFound_2 : Kotlin.throwNPE(), (tmp$9 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname)) != null ? tmp$9 : Kotlin.throwNPE(), (tmp$11 = _.org.kevoree.modeling.api.json.JSONString.unescape((tmp$10 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.objpath)) != null ? tmp$10 : Kotlin.throwNPE())) != null ? tmp$11 : Kotlin.throwNPE()));
                        }
                         else if (traceTypeRead === _.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL.code) {
                          var srcFound_3 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                          if (srcFound_3 == null) {
                            srcFound_3 = previousControlSrc;
                          }
                           else {
                            srcFound_3 = _.org.kevoree.modeling.api.json.JSONString.unescape(srcFound_3);
                          }
                          var tmp$12;
                          this.traces.add_za3rmp$(new _.org.kevoree.modeling.api.trace.ModelRemoveAllTrace(srcFound_3 != null ? srcFound_3 : Kotlin.throwNPE(), (tmp$12 = keys.get_za3rmp$(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname)) != null ? tmp$12 : Kotlin.throwNPE()));
                        }
                         else if (traceTypeRead === _.org.kevoree.modeling.api.util.ActionType.object.RENEW_INDEX.code) {
                        }
                         else {
                          Kotlin.println('Trace lost !!!');
                        }
                      }
                    }
                    currentToken = lexer.nextToken();
                  }
                  return this;
                },
                exportToString: function () {
                  var buffer = new Kotlin.StringBuilder();
                  buffer.append('[');
                  var isFirst = true;
                  var previousSrc = null;
                  var previousType = null;
                  {
                    var tmp$0 = this.traces.iterator();
                    while (tmp$0.hasNext()) {
                      var trace = tmp$0.next();
                      if (!isFirst) {
                        buffer.append(',\n');
                      }
                      if (previousSrc == null || !Kotlin.equals(previousSrc, trace.srcPath)) {
                        buffer.append((new _.org.kevoree.modeling.api.trace.ModelControlTrace(trace.srcPath, null)).toString());
                        buffer.append(',\n');
                        previousSrc = trace.srcPath;
                      }
                      if (previousType == null || !Kotlin.equals(previousType, trace.traceType.code)) {
                        buffer.append((new _.org.kevoree.modeling.api.trace.ModelControlTrace('', trace.traceType.code)).toString());
                        buffer.append(',\n');
                        previousType = trace.traceType.code;
                      }
                      buffer.append(trace.toCString(false, false));
                      isFirst = false;
                    }
                  }
                  buffer.append(']');
                  return buffer.toString();
                },
                toString: function () {
                  return this.exportToString();
                },
                applyOn: function (target) {
                  var traceApplicator = new _.org.kevoree.modeling.api.trace.ModelTraceApplicator(target, this.factory);
                  traceApplicator.applyTraceOnModel(this);
                  return true;
                },
                silentlyApplyOn: function (target) {
                  var traceApplicator = new _.org.kevoree.modeling.api.trace.ModelTraceApplicator(target, this.factory);
                  traceApplicator.fireEvents = false;
                  traceApplicator.applyTraceOnModel(this);
                  return true;
                },
                reverse: function () {
                  var reversed = new Kotlin.ArrayList();
                  var i = _.kotlin.get_size_1(this.traces);
                  while (i > 0) {
                    i = i - 1;
                    reversed.add_za3rmp$(this.traces.get_za3lpa$(i));
                  }
                  this.traces = reversed;
                }
              }),
              ModelTrace: Kotlin.createTrait(null, /** @lends _.org.kevoree.modeling.api.trace.ModelTrace.prototype */ {
                refName: {
                  get: function () {
                    return this.$refName_eb8jwl$;
                  }
                },
                traceType: {
                  get: function () {
                    return this.$traceType_cer0bq$;
                  }
                },
                srcPath: {
                  get: function () {
                    return this.$srcPath_z3ltm8$;
                  }
                },
                toString: function () {
                  return this.toCString(true, true);
                }
              }),
              ModelControlTrace: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.trace.ModelTrace];
              }, function (srcPath, traceTypeGlobal) {
                this.$srcPath_5kjq8d$ = srcPath;
                this.traceTypeGlobal = traceTypeGlobal;
                this.$refName_qcwzy0$ = '';
                this.$traceType_5dhonr$ = _.org.kevoree.modeling.api.util.ActionType.object.CONTROL;
              }, /** @lends _.org.kevoree.modeling.api.trace.ModelControlTrace.prototype */ {
                srcPath: {
                  get: function () {
                    return this.$srcPath_5kjq8d$;
                  }
                },
                refName: {
                  get: function () {
                    return this.$refName_qcwzy0$;
                  }
                },
                traceType: {
                  get: function () {
                    return this.$traceType_5dhonr$;
                  }
                },
                toCString: function (withTypeName, withSrcPath) {
                  var buffer = new Kotlin.StringBuilder();
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.openJSON);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.traceType);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.util.ActionType.object.CONTROL.code);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  if (this.traceTypeGlobal == null) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.srcPath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  }
                   else {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(this.traceTypeGlobal);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  }
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.closeJSON);
                  return buffer.toString();
                }
              }),
              ModelAddTrace: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.trace.ModelTrace];
              }, function (srcPath, refName, previousPath, typeName) {
                this.$srcPath_uvkbsf$ = srcPath;
                this.$refName_a3722s$ = refName;
                this.previousPath = previousPath;
                this.typeName = typeName;
                this.$traceType_2i989x$ = _.org.kevoree.modeling.api.util.ActionType.object.ADD;
              }, /** @lends _.org.kevoree.modeling.api.trace.ModelAddTrace.prototype */ {
                srcPath: {
                  get: function () {
                    return this.$srcPath_uvkbsf$;
                  }
                },
                refName: {
                  get: function () {
                    return this.$refName_a3722s$;
                  }
                },
                traceType: {
                  get: function () {
                    return this.$traceType_2i989x$;
                  }
                },
                toCString: function (withTypeName, withSrcPath) {
                  var buffer = new Kotlin.StringBuilder();
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.openJSON);
                  if (withTypeName) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.traceType);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.util.ActionType.object.ADD.code);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  if (withSrcPath) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.srcPath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(this.refName);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  if (this.previousPath != null) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.previouspath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.previousPath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  }
                  if (this.typeName != null) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.typename);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.typeName);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  }
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.closeJSON);
                  return buffer.toString();
                }
              }),
              ModelAddAllTrace: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.trace.ModelTrace];
              }, function (srcPath, refName, previousPath, typeName) {
                this.$srcPath_1h16pc$ = srcPath;
                this.$refName_m9egez$ = refName;
                this.previousPath = previousPath;
                this.typeName = typeName;
                this.$traceType_pralmu$ = _.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL;
              }, /** @lends _.org.kevoree.modeling.api.trace.ModelAddAllTrace.prototype */ {
                srcPath: {
                  get: function () {
                    return this.$srcPath_1h16pc$;
                  }
                },
                refName: {
                  get: function () {
                    return this.$refName_m9egez$;
                  }
                },
                traceType: {
                  get: function () {
                    return this.$traceType_pralmu$;
                  }
                },
                mkString: function (ss) {
                  if (ss == null) {
                    return null;
                  }
                  var buffer = new Kotlin.StringBuilder();
                  var isFirst = true;
                  {
                    var tmp$0 = ss.iterator();
                    while (tmp$0.hasNext()) {
                      var s = tmp$0.next();
                      if (!isFirst) {
                        buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                      }
                      buffer.append(s);
                      isFirst = false;
                    }
                  }
                  return buffer.toString();
                },
                toCString: function (withTypeName, withSrcPath) {
                  var buffer = new Kotlin.StringBuilder();
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.openJSON);
                  if (withTypeName) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.traceType);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.util.ActionType.object.ADD_ALL.code);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  if (withSrcPath) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.srcPath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(this.refName);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  if (this.previousPath != null) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.previouspath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.mkString(this.previousPath));
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  }
                  if (this.typeName != null) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.typename);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.mkString(this.typeName));
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  }
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.closeJSON);
                  return buffer.toString();
                }
              }),
              ModelRemoveTrace: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.trace.ModelTrace];
              }, function (srcPath, refName, objPath) {
                this.$srcPath_7kbv2k$ = srcPath;
                this.$refName_d81en3$ = refName;
                this.objPath = objPath;
                this.$traceType_po7rum$ = _.org.kevoree.modeling.api.util.ActionType.object.REMOVE;
              }, /** @lends _.org.kevoree.modeling.api.trace.ModelRemoveTrace.prototype */ {
                srcPath: {
                  get: function () {
                    return this.$srcPath_7kbv2k$;
                  }
                },
                refName: {
                  get: function () {
                    return this.$refName_d81en3$;
                  }
                },
                traceType: {
                  get: function () {
                    return this.$traceType_po7rum$;
                  }
                },
                toCString: function (withTypeName, withSrcPath) {
                  var buffer = new Kotlin.StringBuilder();
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.openJSON);
                  if (withTypeName) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.traceType);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE.code);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  if (withSrcPath) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.srcPath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(this.refName);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.objpath);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.objPath);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.closeJSON);
                  return buffer.toString();
                }
              }),
              ModelRemoveAllTrace: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.trace.ModelTrace];
              }, function (srcPath, refName) {
                this.$srcPath_mobmwd$ = srcPath;
                this.$refName_rkf5d4$ = refName;
                this.$traceType_z0g113$ = _.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL;
              }, /** @lends _.org.kevoree.modeling.api.trace.ModelRemoveAllTrace.prototype */ {
                srcPath: {
                  get: function () {
                    return this.$srcPath_mobmwd$;
                  }
                },
                refName: {
                  get: function () {
                    return this.$refName_rkf5d4$;
                  }
                },
                traceType: {
                  get: function () {
                    return this.$traceType_z0g113$;
                  }
                },
                toCString: function (withTypeName, withSrcPath) {
                  var buffer = new Kotlin.StringBuilder();
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.openJSON);
                  if (withTypeName) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.traceType);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.util.ActionType.object.REMOVE_ALL.code);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  if (withSrcPath) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.srcPath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(this.refName);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.closeJSON);
                  return buffer.toString();
                }
              }),
              ModelSetTrace: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.trace.ModelTrace];
              }, function (srcPath, refName, objPath, content, typeName) {
                this.$srcPath_guqstu$ = srcPath;
                this.$refName_xdzzfn$ = refName;
                this.objPath = objPath;
                this.content = content;
                this.typeName = typeName;
                this.$traceType_j5yedg$ = _.org.kevoree.modeling.api.util.ActionType.object.SET;
              }, /** @lends _.org.kevoree.modeling.api.trace.ModelSetTrace.prototype */ {
                srcPath: {
                  get: function () {
                    return this.$srcPath_guqstu$;
                  }
                },
                refName: {
                  get: function () {
                    return this.$refName_xdzzfn$;
                  }
                },
                traceType: {
                  get: function () {
                    return this.$traceType_j5yedg$;
                  }
                },
                toCString: function (withTypeName, withSrcPath) {
                  var buffer = new Kotlin.StringBuilder();
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.openJSON);
                  if (withTypeName) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.traceType);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.util.ActionType.object.SET.code);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  if (withSrcPath) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.src);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.srcPath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                  }
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.refname);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  buffer.append(this.refName);
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  if (this.objPath != null) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.objpath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.objPath);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  }
                  if (this.content != null) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.content);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.content);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  }
                  if (this.typeName != null) {
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.coma);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.typename);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.dp);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                    _.org.kevoree.modeling.api.json.JSONString.encodeBuffer(buffer, this.typeName);
                    buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.bb);
                  }
                  buffer.append(_.org.kevoree.modeling.api.trace.ModelTraceConstants.closeJSON);
                  return buffer.toString();
                }
              }),
              DefaultTraceConverter: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.trace.TraceConverter];
              }, function () {
                this.metaClassNameEquivalence_1_rqkn57$ = new Kotlin.PrimitiveHashMap();
                this.metaClassNameEquivalence_2_rqkn58$ = new Kotlin.PrimitiveHashMap();
                this.attNameEquivalence_1_vwmrr1$ = new Kotlin.PrimitiveHashMap();
                this.attNameEquivalence_2_vwmrr2$ = new Kotlin.PrimitiveHashMap();
              }, /** @lends _.org.kevoree.modeling.api.trace.DefaultTraceConverter.prototype */ {
                addMetaClassEquivalence: function (name1, name2) {
                  this.metaClassNameEquivalence_1_rqkn57$.put_wn2jw4$(name1, name2);
                  this.metaClassNameEquivalence_2_rqkn58$.put_wn2jw4$(name2, name2);
                },
                addAttEquivalence: function (name1, name2) {
                  var fqnArray_1 = Kotlin.splitString(name1, '#');
                  var fqnArray_2 = Kotlin.splitString(name1, '#');
                  this.attNameEquivalence_1_vwmrr1$.put_wn2jw4$(name1, name2);
                  this.attNameEquivalence_2_vwmrr2$.put_wn2jw4$(name2, name2);
                },
                convert: function (trace) {
                  {
                    if (Kotlin.isType(trace, _.org.kevoree.modeling.api.trace.ModelAddTrace)) {
                      var addTrace = trace;
                      var newTrace = new _.org.kevoree.modeling.api.trace.ModelAddTrace(addTrace.srcPath, addTrace.refName, addTrace.previousPath, this.tryConvertClassName(addTrace.typeName));
                      return newTrace;
                    }
                     else if (Kotlin.isType(trace, _.org.kevoree.modeling.api.trace.ModelSetTrace)) {
                      var setTrace = trace;
                      var newTrace_0 = new _.org.kevoree.modeling.api.trace.ModelSetTrace(setTrace.srcPath, setTrace.refName, setTrace.objPath, setTrace.content, this.tryConvertClassName(setTrace.typeName));
                      return newTrace_0;
                    }
                     else {
                      return trace;
                    }
                  }
                },
                tryConvertPath: function (previousPath) {
                  if (previousPath == null) {
                    return null;
                  }
                  return previousPath;
                },
                tryConvertClassName: function (previousClassName) {
                  if (previousClassName == null) {
                    return null;
                  }
                  if (this.metaClassNameEquivalence_1_rqkn57$.containsKey_za3rmp$(previousClassName)) {
                    var tmp$0;
                    return (tmp$0 = this.metaClassNameEquivalence_1_rqkn57$.get_za3rmp$(previousClassName)) != null ? tmp$0 : Kotlin.throwNPE();
                  }
                  if (this.metaClassNameEquivalence_2_rqkn58$.containsKey_za3rmp$(previousClassName)) {
                    var tmp$1;
                    return (tmp$1 = this.metaClassNameEquivalence_2_rqkn58$.get_za3rmp$(previousClassName)) != null ? tmp$1 : Kotlin.throwNPE();
                  }
                  return previousClassName;
                },
                tryConvertAttName: function (previousAttName) {
                  if (previousAttName == null) {
                    return null;
                  }
                  var FQNattName = previousAttName;
                  if (this.attNameEquivalence_1_vwmrr1$.containsKey_za3rmp$(FQNattName)) {
                    var tmp$0;
                    return (tmp$0 = this.attNameEquivalence_1_vwmrr1$.get_za3rmp$(FQNattName)) != null ? tmp$0 : Kotlin.throwNPE();
                  }
                  if (this.attNameEquivalence_2_vwmrr2$.containsKey_za3rmp$(FQNattName)) {
                    var tmp$1;
                    return (tmp$1 = this.attNameEquivalence_2_vwmrr2$.get_za3rmp$(FQNattName)) != null ? tmp$1 : Kotlin.throwNPE();
                  }
                  return previousAttName;
                }
              }),
              TraceConverter: Kotlin.createTrait(null)
            }),
            persistence: Kotlin.definePackage(null, /** @lends _.org.kevoree.modeling.api.persistence */ {
              MemoryDataStore: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.persistence.DataStore];
              }, function () {
                this.selector_38kq2e$ = new _.org.kevoree.modeling.api.persistence.EventDispatcher();
                this.maps = new Kotlin.PrimitiveHashMap();
              }, /** @lends _.org.kevoree.modeling.api.persistence.MemoryDataStore.prototype */ {
                commit: function () {
                },
                register: function (listener, from, to, path) {
                  this.selector_38kq2e$.register(listener, from, to, path);
                },
                unregister: function (listener) {
                  this.selector_38kq2e$.unregister(listener);
                },
                notify: function (event) {
                  this.selector_38kq2e$.dispatch(event);
                },
                getSegmentKeys: function (segment) {
                  if (this.maps.containsKey_za3rmp$(segment)) {
                    var tmp$0;
                    ((tmp$0 = this.maps.get_za3rmp$(segment)) != null ? tmp$0 : Kotlin.throwNPE()).keySet();
                  }
                  return new Kotlin.PrimitiveHashSet();
                },
                getSegments: function () {
                  return this.maps.keySet();
                },
                close: function () {
                  this.selector_38kq2e$.clear();
                  this.maps.clear();
                },
                getOrCreateSegment: function (segment) {
                  if (!this.maps.containsKey_za3rmp$(segment)) {
                    this.maps.put_wn2jw4$(segment, new Kotlin.PrimitiveHashMap());
                  }
                  var tmp$0;
                  return (tmp$0 = this.maps.get_za3rmp$(segment)) != null ? tmp$0 : Kotlin.throwNPE();
                },
                put: function (segment, key, value) {
                  this.getOrCreateSegment(segment).put_wn2jw4$(key, value);
                },
                get: function (segment, key) {
                  return this.getOrCreateSegment(segment).get_za3rmp$(key);
                },
                remove: function (segment, key) {
                  this.getOrCreateSegment(segment).remove_za3rmp$(key);
                },
                dump: function () {
                  {
                    var tmp$0 = _.kotlin.iterator_s8ckw1$(this.maps);
                    while (tmp$0.hasNext()) {
                      var k = tmp$0.next();
                      Kotlin.println('Map ' + _.kotlin.get_key(k));
                      {
                        var tmp$1 = _.kotlin.iterator_s8ckw1$(_.kotlin.get_value(k));
                        while (tmp$1.hasNext()) {
                          var t = tmp$1.next();
                          Kotlin.println(_.kotlin.get_key(t) + '->' + _.kotlin.get_value(t));
                        }
                      }
                    }
                  }
                }
              }),
              EventDispatcher: Kotlin.createClass(null, function () {
                this.listeners_3hhuzx$ = new Kotlin.ComplexHashMap();
              }, /** @lends _.org.kevoree.modeling.api.persistence.EventDispatcher.prototype */ {
                register: function (listener, from, to, pathRegex) {
                  this.listeners_3hhuzx$.put_wn2jw4$(listener, new _.org.kevoree.modeling.api.persistence.TimedRegistration(from, to, pathRegex));
                },
                unregister: function (listener) {
                  this.listeners_3hhuzx$.remove_za3rmp$(listener);
                },
                dispatch: function (event) {
                  {
                    var tmp$0 = _.kotlin.iterator_s8ckw1$(this.listeners_3hhuzx$);
                    while (tmp$0.hasNext()) {
                      var l = tmp$0.next();
                      if (_.kotlin.get_value(l).covered(event)) {
                        _.kotlin.get_key(l).elementChanged(event);
                      }
                    }
                  }
                },
                clear: function () {
                  this.listeners_3hhuzx$.clear();
                }
              }),
              TimedRegistration: Kotlin.createClass(null, function (from, to, pathRegex) {
                this.from = from;
                this.to = to;
                this.pathRegex = pathRegex;
              }, /** @lends _.org.kevoree.modeling.api.persistence.TimedRegistration.prototype */ {
                covered: function (event) {
                  if (Kotlin.isType(event.source, _.org.kevoree.modeling.api.time.TimeAwareKMFContainer)) {
                    if (this.from != null) {
                      if (this.from < event.source.now) {
                        return false;
                      }
                    }
                    if (this.to != null) {
                      if (this.to < event.source.now) {
                        return false;
                      }
                    }
                  }
                  if (event.source != null) {
                    if (this.pathRegex.contains('*')) {
                      var regexPath = this.pathRegex.replace('*', '.*');
                      return _.js.matches_94jgcu$(event.source.path(), regexPath);
                    }
                     else {
                      return Kotlin.equals(event.source.path(), this.pathRegex);
                    }
                  }
                   else {
                    return false;
                  }
                },
                component1: function () {
                  return this.from;
                },
                component2: function () {
                  return this.to;
                },
                component3: function () {
                  return this.pathRegex;
                },
                copy: function (from, to, pathRegex) {
                  return new _.org.kevoree.modeling.api.persistence.TimedRegistration(from === void 0 ? this.from : from, to === void 0 ? this.to : to, pathRegex === void 0 ? this.pathRegex : pathRegex);
                },
                toString: function () {
                  return 'TimedRegistration(from=' + Kotlin.toString(this.from) + (', to=' + Kotlin.toString(this.to)) + (', pathRegex=' + Kotlin.toString(this.pathRegex)) + ')';
                },
                hashCode: function () {
                  var result = -551475360;
                  result = result * 31 + Kotlin.hashCode(this.from) | 0;
                  result = result * 31 + Kotlin.hashCode(this.to) | 0;
                  result = result * 31 + Kotlin.hashCode(this.pathRegex) | 0;
                  return result;
                },
                equals_za3rmp$: function (other) {
                  return this === other || (other !== null && (Object.getPrototypeOf(this) === Object.getPrototypeOf(other) && (Kotlin.equals(this.from, other.from) && Kotlin.equals(this.to, other.to) && Kotlin.equals(this.pathRegex, other.pathRegex))));
                }
              }),
              PersistenceKMFFactory: Kotlin.createTrait(function () {
                return [_.org.kevoree.modeling.api.events.ModelElementListener, _.org.kevoree.modeling.api.KMFFactory];
              }, /** @lends _.org.kevoree.modeling.api.persistence.PersistenceKMFFactory.prototype */ {
                datastore: {
                  get: function () {
                    return this.$datastore_xkqfe9$;
                  }
                },
                dirty: {
                  get: function () {
                    return this.$dirty_e66hhy$;
                  },
                  set: function (tmp$0) {
                    this.$dirty_e66hhy$ = tmp$0;
                  }
                },
                originTransaction: {
                  get: function () {
                    return this.$originTransaction_kdsx68$;
                  }
                },
                remove: function (elem) {
                  this.datastore.remove(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), elem.path());
                  this.datastore.remove('type', elem.path());
                  this.elem_cache.remove_za3rmp$(elem.path());
                  this.modified_elements.remove_za3rmp$(Kotlin.hashCode(elem).toString() + elem.internalGetKey());
                },
                elem_cache: {
                  get: function () {
                    return this.$elem_cache_55i3ba$;
                  }
                },
                elementsToBeRemoved: {
                  get: function () {
                    return this.$elementsToBeRemoved_qnzocd$;
                  }
                },
                modified_elements: {
                  get: function () {
                    return this.$modified_elements_qtc91h$;
                  }
                },
                notify: function (elem) {
                  if (elem.internalGetKey() != null) {
                    var key = Kotlin.hashCode(elem).toString() + elem.internalGetKey();
                    if (this.modified_elements.get_za3rmp$(key) == null) {
                      this.modified_elements.put_wn2jw4$(key, elem);
                    }
                    if (elem.path().startsWith('/')) {
                      this.elem_cache.put_wn2jw4$(elem.path(), elem);
                    }
                  }
                  if (Kotlin.isType(elem, _.org.kevoree.modeling.api.persistence.KMFContainerProxy) && !elem.isDirty) {
                    elem.isDirty = true;
                  }
                },
                cleanUnusedPaths: function (path) {
                  this.datastore.remove(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), path);
                  this.datastore.remove('type', path);
                  this.elem_cache.remove_za3rmp$(path);
                },
                persist: function (elem) {
                  if (Kotlin.isType(elem, _.org.kevoree.modeling.api.persistence.KMFContainerProxy) && !elem.isDirty) {
                    return;
                  }
                  var elemPath = elem.path();
                  if (Kotlin.equals(elemPath, '')) {
                    throw new Error('Internal error, empty path found during persist method ' + elem);
                  }
                  if (!elemPath.startsWith('/')) {
                    throw new Error('Cannot persist, because the path of the element do not refer to a root: ' + elemPath + ' -> ' + elem);
                  }
                  var traces = elem.toTraces(true, true);
                  var traceSeq = new _.org.kevoree.modeling.api.trace.TraceSequence(this);
                  traceSeq.populate(traces);
                  this.datastore.put(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), elemPath, traceSeq.exportToString());
                  var castedInBounds = elem;
                  var saved = _.org.kevoree.modeling.api.time.blob.MetaHelper.serialize(castedInBounds.internal_inboundReferences);
                  this.datastore.put(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), elemPath + '#', saved);
                  this.datastore.put('type', elemPath, elem.metaClassName());
                  if (Kotlin.isType(elem, _.org.kevoree.modeling.api.persistence.KMFContainerProxy)) {
                    elem.originFactory = this;
                  }
                },
                endCommit: function () {
                  this.datastore.commit();
                },
                commit: function () {
                  if (!this.dirty) {
                    return;
                  }
                  var keys = _.kotlin.toList_h3panj$(this.modified_elements.keySet());
                  {
                    var tmp$0 = keys.iterator();
                    while (tmp$0.hasNext()) {
                      var elem = tmp$0.next();
                      var resolved = this.modified_elements.get_za3rmp$(elem);
                      if (resolved != null) {
                        if (!resolved.path().startsWith('/')) {
                          if (!resolved.isDeleted()) {
                            resolved.delete();
                          }
                          this.modified_elements.remove_za3rmp$(elem);
                        }
                      }
                    }
                  }
                  {
                    var tmp$1 = this.modified_elements.keySet().iterator();
                    while (tmp$1.hasNext()) {
                      var elemKey = tmp$1.next();
                      var tmp$2;
                      var elem_0 = (tmp$2 = this.modified_elements.get_za3rmp$(elemKey)) != null ? tmp$2 : Kotlin.throwNPE();
                      this.persist(elem_0);
                      this.elementsToBeRemoved.remove_za3rmp$(elem_0.path());
                    }
                  }
                  {
                    var tmp$3 = this.elementsToBeRemoved.iterator();
                    while (tmp$3.hasNext()) {
                      var e = tmp$3.next();
                      this.cleanUnusedPaths(e);
                    }
                  }
                },
                clear: function () {
                  {
                    var tmp$0 = this.elem_cache.keySet().iterator();
                    while (tmp$0.hasNext()) {
                      var elemKey = tmp$0.next();
                      var tmp$1;
                      var elem = (tmp$1 = this.elem_cache.get_za3rmp$(elemKey)) != null ? tmp$1 : Kotlin.throwNPE();
                      elem.removeModelElementListener(this);
                    }
                  }
                  this.elem_cache.clear();
                  this.modified_elements.clear();
                  this.elementsToBeRemoved.clear();
                },
                elementChanged: function (evt) {
                  var tmp$0;
                  ((tmp$0 = evt.source) != null ? tmp$0 : Kotlin.throwNPE()).isDirty = true;
                  this.notify(evt.source);
                },
                monitor: function (elem) {
                  if (!this.dirty) {
                    this.dirty = true;
                  }
                  elem.addModelElementListener(this);
                },
                lookup: function (path) {
                  if (Kotlin.equals(path, '')) {
                    return null;
                  }
                  if (this.elem_cache.containsKey_za3rmp$(path)) {
                    return this.elem_cache.get_za3rmp$(path);
                  }
                  var typeName = this.datastore.get('type', path);
                  if (typeName != null) {
                    var tmp$0;
                    var elem = (tmp$0 = this.create(typeName)) != null ? tmp$0 : Kotlin.throwNPE();
                    this.elem_cache.put_wn2jw4$(path, elem);
                    elem.isResolved = false;
                    elem.setOriginPath(path);
                    this.monitor(elem);
                    return elem;
                  }
                  return null;
                },
                getTraces: function (origin) {
                  var sequence = new _.org.kevoree.modeling.api.trace.TraceSequence(this);
                  var traces = this.datastore.get(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), origin.path());
                  if (traces != null) {
                    sequence.populateFromString(traces);
                    return sequence;
                  }
                  return null;
                },
                loadInbounds: function (elem) {
                  var castedInBounds = elem;
                  var payload = this.datastore.get(_.org.kevoree.modeling.api.time.TimeSegment.object.RAW.name(), elem.path() + '#');
                  if (payload != null) {
                    castedInBounds.internal_inboundReferences = _.org.kevoree.modeling.api.time.blob.MetaHelper.unserialize(payload, this);
                  }
                },
                select: function (query) {
                  var localRoot = this.lookup('/');
                  if (localRoot != null && Kotlin.equals(query, '/')) {
                    var result = new Kotlin.ArrayList();
                    result.add_za3rmp$(localRoot);
                    return result;
                  }
                  if (localRoot != null) {
                    return localRoot.select(query);
                  }
                   else {
                    return new Kotlin.ArrayList();
                  }
                }
              }),
              KMFContainerProxy: Kotlin.createTrait(function () {
                return [_.org.kevoree.modeling.api.KMFContainer];
              }, /** @lends _.org.kevoree.modeling.api.persistence.KMFContainerProxy.prototype */ {
                isResolved: {
                  get: function () {
                    return this.$isResolved_q9gcci$;
                  },
                  set: function (tmp$0) {
                    this.$isResolved_q9gcci$ = tmp$0;
                  }
                },
                inResolution: {
                  get: function () {
                    return this.$inResolution_fvhr0z$;
                  },
                  set: function (tmp$0) {
                    this.$inResolution_fvhr0z$ = tmp$0;
                  }
                },
                isDirty: {
                  get: function () {
                    return this.$isDirty_z1d6gk$;
                  },
                  set: function (tmp$0) {
                    this.$isDirty_z1d6gk$ = tmp$0;
                  }
                },
                originFactory: {
                  get: function () {
                    return this.$originFactory_8fzws8$;
                  },
                  set: function (tmp$0) {
                    this.$originFactory_8fzws8$ = tmp$0;
                  }
                },
                relativeLookupFrom: function (base, relationInParent, key) {
                  var currentPath = base.path();
                  if (Kotlin.equals(currentPath, '/')) {
                    var tmp$0;
                    return (tmp$0 = this.originFactory) != null ? tmp$0.lookup('/' + relationInParent + '[' + key + ']') : null;
                  }
                   else {
                    var tmp$1;
                    return (tmp$1 = this.originFactory) != null ? tmp$1.lookup(currentPath + '/' + relationInParent + '[' + key + ']') : null;
                  }
                }
              }),
              AbstractDataStore: Kotlin.createClass(function () {
                return [_.org.kevoree.modeling.api.persistence.DataStore];
              }, function () {
                this.selector_6kse09$ = new _.org.kevoree.modeling.api.persistence.EventDispatcher();
              }, /** @lends _.org.kevoree.modeling.api.persistence.AbstractDataStore.prototype */ {
                register: function (listener, from, to, path) {
                  this.selector_6kse09$.register(listener, from, to, path);
                },
                unregister: function (listener) {
                  this.selector_6kse09$.unregister(listener);
                },
                notify: function (event) {
                  this.selector_6kse09$.dispatch(event);
                }
              }),
              DataStore: Kotlin.createTrait(null)
            })
          })
        }),
        log: Kotlin.definePackage(function () {
          this.Log = Kotlin.createObject(null, function () {
            this.LEVEL_NONE = 6;
            this.LEVEL_ERROR = 5;
            this.LEVEL_WARN = 4;
            this.LEVEL_INFO = 3;
            this.LEVEL_DEBUG = 2;
            this.LEVEL_TRACE = 1;
            this.$level_qhmnt5$ = this.LEVEL_INFO;
            this._ERROR_oj0992$ = this.level <= this.LEVEL_ERROR;
            this._WARN_qp2148$ = this.level <= this.LEVEL_WARN;
            this._INFO_qpapkw$ = this.level <= this.LEVEL_INFO;
            this._DEBUG_oi7u3l$ = this.level <= this.LEVEL_DEBUG;
            this._TRACE_or8t8z$ = this.level <= this.LEVEL_TRACE;
            this.logger = new _.org.kevoree.log.Logger();
            this.beginParam = '{';
            this.endParam = '}';
          }, {
            level: {
              get: function () {
                return this.$level_qhmnt5$;
              },
              set: function (newLevel) {
                this.$level_qhmnt5$ = newLevel;
                this._ERROR_oj0992$ = newLevel <= this.LEVEL_ERROR;
                this._WARN_qp2148$ = newLevel <= this.LEVEL_WARN;
                this._INFO_qpapkw$ = newLevel <= this.LEVEL_INFO;
                this._DEBUG_oi7u3l$ = newLevel <= this.LEVEL_DEBUG;
                this._TRACE_or8t8z$ = newLevel <= this.LEVEL_TRACE;
              }
            },
            NONE: function () {
              this.level = this.LEVEL_NONE;
            },
            ERROR: function () {
              this.level = this.LEVEL_ERROR;
            },
            WARN: function () {
              this.level = this.LEVEL_WARN;
            },
            INFO: function () {
              this.level = this.LEVEL_INFO;
            },
            DEBUG: function () {
              this.level = this.LEVEL_DEBUG;
            },
            TRACE: function () {
              this.level = this.LEVEL_TRACE;
            },
            processMessage: function (message, p1, p2, p3, p4, p5) {
              if (p1 == null) {
                return message;
              }
              var buffer = new Kotlin.StringBuilder();
              var previousCharfound = false;
              var param = 0;
              var i = 0;
              while (i < message.length) {
                var currentChar = message.charAt(i);
                if (previousCharfound) {
                  if (currentChar === this.endParam) {
                    param++;
                    {
                      if (param === 1) {
                        buffer = new Kotlin.StringBuilder();
                        buffer.append(message.substring(0, i - 1));
                        buffer.append((p1 != null ? p1 : Kotlin.throwNPE()).toString());
                      }
                       else if (param === 2) {
                        buffer.append((p2 != null ? p2 : Kotlin.throwNPE()).toString());
                      }
                       else if (param === 3) {
                        buffer.append((p3 != null ? p3 : Kotlin.throwNPE()).toString());
                      }
                       else if (param === 4) {
                        buffer.append((p4 != null ? p4 : Kotlin.throwNPE()).toString());
                      }
                       else if (param === 5) {
                        buffer.append((p5 != null ? p5 : Kotlin.throwNPE()).toString());
                      }
                       else {
                      }
                    }
                    previousCharfound = false;
                  }
                   else {
                    if (buffer != null) {
                      message.charAt(i - 1);
                      buffer.append(currentChar);
                    }
                    previousCharfound = false;
                  }
                }
                 else {
                  if (currentChar === this.beginParam) {
                    previousCharfound = true;
                  }
                   else {
                    if (buffer != null) {
                      buffer.append(currentChar);
                    }
                  }
                }
                i = i + 1;
              }
              if (buffer != null) {
                return buffer.toString();
              }
               else {
                return message;
              }
            },
            error_1: function (message, ex, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._ERROR_oj0992$) {
                this.internal_error(this.processMessage(message, p1, p2, p3, p4, p5), ex);
              }
            },
            error: function (message, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._ERROR_oj0992$) {
                this.internal_error(this.processMessage(message, p1, p2, p3, p4, p5), null);
              }
            },
            internal_error: function (message, ex) {
              this.logger.log(this.LEVEL_ERROR, message, ex);
            },
            warn_1: function (message, ex, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._WARN_qp2148$) {
                this.internal_warn(this.processMessage(message, p1, p2, p3, p4, p5), ex);
              }
            },
            warn: function (message, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._WARN_qp2148$) {
                this.internal_warn(this.processMessage(message, p1, p2, p3, p4, p5), null);
              }
            },
            internal_warn: function (message, ex) {
              this.logger.log(this.LEVEL_WARN, message, ex);
            },
            info_1: function (message, ex, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._INFO_qpapkw$) {
                this.internal_info(this.processMessage(message, p1, p2, p3, p4, p5), ex);
              }
            },
            info: function (message, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._INFO_qpapkw$) {
                this.internal_info(this.processMessage(message, p1, p2, p3, p4, p5), null);
              }
            },
            internal_info: function (message, ex) {
              this.logger.log(this.LEVEL_INFO, message, ex);
            },
            debug_1: function (message, ex, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._DEBUG_oi7u3l$) {
                this.internal_debug(this.processMessage(message, p1, p2, p3, p4, p5), ex);
              }
            },
            debug: function (message, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._DEBUG_oi7u3l$) {
                this.internal_debug(this.processMessage(message, p1, p2, p3, p4, p5), null);
              }
            },
            internal_debug: function (message, ex) {
              this.logger.log(this.LEVEL_DEBUG, message, ex);
            },
            trace_1: function (message, ex, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._TRACE_or8t8z$) {
                this.internal_trace(this.processMessage(message, p1, p2, p3, p4, p5), ex);
              }
            },
            trace: function (message, p1, p2, p3, p4, p5) {
              if (p1 === void 0)
                p1 = null;
              if (p2 === void 0)
                p2 = null;
              if (p3 === void 0)
                p3 = null;
              if (p4 === void 0)
                p4 = null;
              if (p5 === void 0)
                p5 = null;
              if (this._TRACE_or8t8z$) {
                this.internal_trace(this.processMessage(message, p1, p2, p3, p4, p5), null);
              }
            },
            internal_trace: function (message, ex) {
              this.logger.log(this.LEVEL_TRACE, message, ex);
            }
          });
        }, /** @lends _.org.kevoree.log */ {
          Logger: Kotlin.createClass(null, function () {
            this.firstLogTime = (new Date()).getTime();
            this.error_msg = ' ERROR: ';
            this.warn_msg = ' WARN: ';
            this.info_msg = ' INFO: ';
            this.debug_msg = ' DEBUG: ';
            this.trace_msg = ' TRACE: ';
            this.category = null;
          }, /** @lends _.org.kevoree.log.Logger.prototype */ {
            setCategory: function (category) {
              this.category = category;
            },
            log: function (level, message, ex) {
              var builder = new Kotlin.StringBuilder();
              var time = (new Date()).getTime() - this.firstLogTime;
              var minutes = time / (1000 * 60) | 0;
              var seconds = (time / 1000 | 0) % 60;
              if (minutes <= 9)
                builder.append('0');
              builder.append(minutes.toString());
              builder.append(':');
              if (seconds <= 9)
                builder.append('0');
              builder.append(seconds.toString());
              {
                if (level === _.org.kevoree.log.Log.LEVEL_ERROR) {
                  builder.append(this.error_msg);
                }
                 else if (level === _.org.kevoree.log.Log.LEVEL_WARN) {
                  builder.append(this.warn_msg);
                }
                 else if (level === _.org.kevoree.log.Log.LEVEL_INFO) {
                  builder.append(this.info_msg);
                }
                 else if (level === _.org.kevoree.log.Log.LEVEL_DEBUG) {
                  builder.append(this.debug_msg);
                }
                 else if (level === _.org.kevoree.log.Log.LEVEL_TRACE) {
                  builder.append(this.trace_msg);
                }
                 else {
                }
              }
              if (this.category != null) {
                builder.append('[');
                var tmp$0;
                builder.append(((tmp$0 = this.category) != null ? tmp$0 : Kotlin.throwNPE()).toString());
                builder.append('] ');
              }
              builder.append(message);
              if (ex != null) {
                builder.append(Kotlin.toString(ex.getMessage()));
              }
              {
                if (level === _.org.kevoree.log.Log.LEVEL_ERROR) {
                  console.error(builder.toString());
                }
                 else if (level === _.org.kevoree.log.Log.LEVEL_WARN) {
                  console.warn(builder.toString());
                }
                 else if (level === _.org.kevoree.log.Log.LEVEL_INFO) {
                  console.info(builder.toString());
                }
                 else if (level === _.org.kevoree.log.Log.LEVEL_DEBUG) {
                  console.log(builder.toString());
                }
                 else if (level === _.org.kevoree.log.Log.LEVEL_TRACE) {
                  console.log(builder.toString());
                }
                 else {
                }
              }
            }
          })
        })
      }),
      w3c: Kotlin.definePackage(null, /** @lends _.org.w3c */ {
        dom: Kotlin.definePackage(null, /** @lends _.org.w3c.dom */ {
          events: Kotlin.definePackage(null, /** @lends _.org.w3c.dom.events */ {
            EventListener: Kotlin.createTrait(null)
          })
        })
      })
    }),
    kotlin: Kotlin.definePackage(function () {
      this.stdlib_emptyList_w9bu57$ = new Kotlin.ArrayList();
      this.stdlib_emptyMap_h2vi7z$ = new Kotlin.ComplexHashMap();
    }, /** @lends _.kotlin */ {
      dom: Kotlin.definePackage(null, /** @lends _.kotlin.dom */ {
        createDocument: function () {
          return document.implementation.createDocument(null, null, null);
        },
        toXmlString_asww5t$: function ($receiver) {
          return $receiver.outerHTML;
        },
        toXmlString_rq0l4m$: function ($receiver, xmlDeclaration) {
          return $receiver.outerHTML;
        },
        eventHandler: function (handler) {
          return new _.kotlin.dom.EventListenerHandler(handler);
        },
        EventListenerHandler: Kotlin.createClass(function () {
          return [_.org.w3c.dom.events.EventListener];
        }, function (handler) {
          this.handler = handler;
        }, /** @lends _.kotlin.dom.EventListenerHandler.prototype */ {
          handleEvent_9ojx7i$: function (e) {
            if (e != null) {
              this.handler(e);
            }
          }
        }),
        mouseEventHandler$f: function (handler) {
          return function (e) {
            if (Kotlin.isType(e, MouseEvent)) {
              handler(e);
            }
          };
        },
        mouseEventHandler: function (handler) {
          return _.kotlin.dom.eventHandler(_.kotlin.dom.mouseEventHandler$f(handler));
        },
        on_10gtds$: function ($receiver, name, capture, handler) {
          return _.kotlin.dom.on_edii0a$($receiver, name, capture, _.kotlin.dom.eventHandler(handler));
        },
        on_edii0a$: function ($receiver, name, capture, listener) {
          var tmp$0;
          if (Kotlin.isType($receiver, EventTarget)) {
            $receiver.addEventListener(name, listener, capture);
            tmp$0 = new _.kotlin.dom.CloseableEventListener($receiver, listener, name, capture);
          }
           else {
            tmp$0 = null;
          }
          return tmp$0;
        },
        CloseableEventListener: Kotlin.createClass(function () {
          return [Kotlin.Closeable];
        }, function (target, listener, name, capture) {
          this.target = target;
          this.listener = listener;
          this.name = name;
          this.capture = capture;
        }, /** @lends _.kotlin.dom.CloseableEventListener.prototype */ {
          close: function () {
            this.target.removeEventListener(this.name, this.listener, this.capture);
          }
        }),
        onClick_alenf6$: function ($receiver, capture, handler) {
          if (capture === void 0)
            capture = false;
          return _.kotlin.dom.on_edii0a$($receiver, 'click', capture, _.kotlin.dom.mouseEventHandler(handler));
        },
        onDoubleClick_alenf6$: function ($receiver, capture, handler) {
          if (capture === void 0)
            capture = false;
          return _.kotlin.dom.on_edii0a$($receiver, 'dblclick', capture, _.kotlin.dom.mouseEventHandler(handler));
        },
        emptyElementList: function () {
          return Kotlin.emptyList();
        },
        emptyNodeList: function () {
          return Kotlin.emptyList();
        },
        get_text: {value: function ($receiver) {
          return $receiver.textContent;
        }},
        set_text: {value: function ($receiver, value) {
          $receiver.textContent = value;
        }},
        get_childrenText: {value: function ($receiver) {
          var buffer = new Kotlin.StringBuilder();
          var nodeList = $receiver.childNodes;
          var i = 0;
          var size = nodeList.length;
          while (i < size) {
            var node = nodeList.item(i);
            if (node != null) {
              if (_.kotlin.dom.isText(node)) {
                buffer.append(node.nodeValue);
              }
            }
            i++;
          }
          return buffer.toString();
        }},
        set_childrenText: {value: function ($receiver, value) {
          var element = $receiver;
          {
            var tmp$0 = _.kotlin.dom.children(element).iterator();
            while (tmp$0.hasNext()) {
              var node = tmp$0.next();
              if (_.kotlin.dom.isText(node)) {
                $receiver.removeChild(node);
              }
            }
          }
          _.kotlin.dom.addText(element, value);
        }},
        get_id: {value: function ($receiver) {
          var tmp$0;
          return (tmp$0 = $receiver.getAttribute('id')) != null ? tmp$0 : '';
        }},
        set_id: {value: function ($receiver, value) {
          $receiver.setAttribute('id', value);
          $receiver.setIdAttribute('id', true);
        }},
        get_style: {value: function ($receiver) {
          var tmp$0;
          return (tmp$0 = $receiver.getAttribute('style')) != null ? tmp$0 : '';
        }},
        set_style: {value: function ($receiver, value) {
          $receiver.setAttribute('style', value);
        }},
        get_classes: {value: function ($receiver) {
          var tmp$0;
          return (tmp$0 = $receiver.getAttribute('class')) != null ? tmp$0 : '';
        }},
        set_classes: {value: function ($receiver, value) {
          $receiver.setAttribute('class', value);
        }},
        hasClass: function ($receiver, cssClass) {
          var c = _.kotlin.dom.get_classes($receiver);
          return _.js.matches_94jgcu$(c, '(^|.*' + '\\' + 's+)' + cssClass + '(' + '$' + '|' + '\\' + 's+.*)');
        },
        children: function ($receiver) {
          return _.kotlin.dom.toList($receiver != null ? $receiver.childNodes : null);
        },
        childElements$f: function (it) {
          return it.nodeType === Node.ELEMENT_NODE;
        },
        childElements$f_0: function (it) {
          return it;
        },
        childElements: function ($receiver) {
          return _.kotlin.map_vqr6wr$(_.kotlin.filter_vqr6wr$(_.kotlin.dom.children($receiver), _.kotlin.dom.childElements$f), _.kotlin.dom.childElements$f_0);
        },
        childElements_1$f: function (name) {
          return function (it) {
            return it.nodeType === Node.ELEMENT_NODE && Kotlin.equals(it.nodeName, name);
          };
        },
        childElements_1$f_0: function (it) {
          return it;
        },
        childElements_1: function ($receiver, name) {
          return _.kotlin.map_vqr6wr$(_.kotlin.filter_vqr6wr$(_.kotlin.dom.children($receiver), _.kotlin.dom.childElements_1$f(name)), _.kotlin.dom.childElements_1$f_0);
        },
        get_elements: {value: function ($receiver) {
          return _.kotlin.dom.toElementList($receiver != null ? $receiver.getElementsByTagName('*') : null);
        }},
        get_elements_0: {value: function ($receiver) {
          return _.kotlin.dom.toElementList($receiver != null ? $receiver.getElementsByTagName('*') : null);
        }},
        elements_1: function ($receiver, localName) {
          return _.kotlin.dom.toElementList($receiver != null ? $receiver.getElementsByTagName(localName) : null);
        },
        elements_2: function ($receiver, localName) {
          return _.kotlin.dom.toElementList($receiver != null ? $receiver.getElementsByTagName(localName) : null);
        },
        elements_3: function ($receiver, namespaceUri, localName) {
          return _.kotlin.dom.toElementList($receiver != null ? $receiver.getElementsByTagNameNS(namespaceUri, localName) : null);
        },
        elements_2_0: function ($receiver, namespaceUri, localName) {
          return _.kotlin.dom.toElementList($receiver != null ? $receiver.getElementsByTagNameNS(namespaceUri, localName) : null);
        },
        toList: function ($receiver) {
          var tmp$0;
          if ($receiver == null) {
            tmp$0 = _.kotlin.dom.emptyNodeList();
          }
           else {
            tmp$0 = new _.kotlin.dom.NodeListAsList($receiver);
          }
          return tmp$0;
        },
        toElementList: function ($receiver) {
          var tmp$0;
          if ($receiver == null) {
            tmp$0 = new Kotlin.ArrayList();
          }
           else {
            tmp$0 = new _.kotlin.dom.ElementListAsList($receiver);
          }
          return tmp$0;
        },
        get$f: function (selector) {
          return function (it) {
            return _.kotlin.dom.hasClass(it, selector.substring(1));
          };
        },
        get: function ($receiver, selector) {
          var root = $receiver != null ? $receiver.documentElement : null;
          var tmp$0;
          if (root != null) {
            if (Kotlin.equals(selector, '*')) {
              tmp$0 = _.kotlin.dom.get_elements($receiver);
            }
             else if (selector.startsWith('.')) {
              tmp$0 = _.kotlin.toList_h3panj$(_.kotlin.filter_vqr6wr$(_.kotlin.dom.get_elements($receiver), _.kotlin.dom.get$f(selector)));
            }
             else if (selector.startsWith('#')) {
              var id = selector.substring(1);
              var element = $receiver != null ? $receiver.getElementById(id) : null;
              return element != null ? _.kotlin.arrayListOf_mzrxf8$([element]) : _.kotlin.dom.emptyElementList();
            }
             else {
              tmp$0 = _.kotlin.dom.elements_2($receiver, selector);
            }
          }
           else {
            tmp$0 = _.kotlin.dom.emptyElementList();
          }
          return tmp$0;
        },
        get_1$f: function (selector) {
          return function (it) {
            return _.kotlin.dom.hasClass(it, selector.substring(1));
          };
        },
        get_1: function ($receiver, selector) {
          var tmp$1;
          if (Kotlin.equals(selector, '*')) {
            tmp$1 = _.kotlin.dom.get_elements_0($receiver);
          }
           else if (selector.startsWith('.')) {
            tmp$1 = _.kotlin.toList_h3panj$(_.kotlin.filter_vqr6wr$(_.kotlin.dom.get_elements_0($receiver), _.kotlin.dom.get_1$f(selector)));
          }
           else if (selector.startsWith('#')) {
            var tmp$0;
            var element = (tmp$0 = $receiver.ownerDocument) != null ? tmp$0.getElementById(selector.substring(1)) : null;
            return element != null ? _.kotlin.arrayListOf_mzrxf8$([element]) : _.kotlin.dom.emptyElementList();
          }
           else {
            tmp$1 = _.kotlin.dom.elements_1($receiver, selector);
          }
          return tmp$1;
        },
        NodeListAsList: Kotlin.createClass(function () {
          return [Kotlin.AbstractList];
        }, function $fun(nodeList) {
          $fun.baseInitializer.call(this);
          this.nodeList = nodeList;
        }, /** @lends _.kotlin.dom.NodeListAsList.prototype */ {
          get_za3lpa$: function (index) {
            var node = this.nodeList.item(index);
            if (node == null) {
              throw new RangeError('NodeList does not contain a node at index: ' + index);
            }
             else {
              return node;
            }
          },
          size: function () {
            return this.nodeList.length;
          }
        }),
        ElementListAsList: Kotlin.createClass(function () {
          return [Kotlin.AbstractList];
        }, function $fun(nodeList) {
          $fun.baseInitializer.call(this);
          this.nodeList = nodeList;
        }, /** @lends _.kotlin.dom.ElementListAsList.prototype */ {
          get_za3lpa$: function (index) {
            var node = this.nodeList.item(index);
            if (node == null) {
              throw new RangeError('NodeList does not contain a node at index: ' + index);
            }
             else if (node.nodeType === Node.ELEMENT_NODE) {
              return node;
            }
             else {
              throw new Kotlin.IllegalArgumentException('Node is not an Element as expected but is ' + node);
            }
          },
          size: function () {
            return this.nodeList.length;
          }
        }),
        clear: function ($receiver) {
          while (true) {
            var child = $receiver.firstChild;
            if (child == null) {
              return;
            }
             else {
              $receiver.removeChild(child);
            }
          }
        },
        nextSiblings: function ($receiver) {
          return new _.kotlin.dom.NextSiblings($receiver);
        },
        NextSiblings: Kotlin.createClass(null, function (node) {
          this.node = node;
        }, /** @lends _.kotlin.dom.NextSiblings.prototype */ {
          iterator: function () {
            return _.kotlin.dom.NextSiblings.iterator$f(this);
          }
        }, /** @lends _.kotlin.dom.NextSiblings */ {
          iterator$f: function (this$NextSiblings) {
            return Kotlin.createObject(function () {
              return [_.kotlin.support.AbstractIterator];
            }, function $fun() {
              $fun.baseInitializer.call(this);
            }, {
              computeNext: function () {
                var nextValue = this$NextSiblings.node.nextSibling;
                if (nextValue != null) {
                  this.setNext_za3rmp$(nextValue);
                  this$NextSiblings.node = nextValue;
                }
                 else {
                  this.done();
                }
              }
            });
          }
        }),
        previousSiblings: function ($receiver) {
          return new _.kotlin.dom.PreviousSiblings($receiver);
        },
        PreviousSiblings: Kotlin.createClass(null, function (node) {
          this.node = node;
        }, /** @lends _.kotlin.dom.PreviousSiblings.prototype */ {
          iterator: function () {
            return _.kotlin.dom.PreviousSiblings.iterator$f(this);
          }
        }, /** @lends _.kotlin.dom.PreviousSiblings */ {
          iterator$f: function (this$PreviousSiblings) {
            return Kotlin.createObject(function () {
              return [_.kotlin.support.AbstractIterator];
            }, function $fun() {
              $fun.baseInitializer.call(this);
            }, {
              computeNext: function () {
                var nextValue = this$PreviousSiblings.node.previousSibling;
                if (nextValue != null) {
                  this.setNext_za3rmp$(nextValue);
                  this$PreviousSiblings.node = nextValue;
                }
                 else {
                  this.done();
                }
              }
            });
          }
        }),
        isText: function ($receiver) {
          var nt = $receiver.nodeType;
          return nt === Node.TEXT_NODE || nt === Node.CDATA_SECTION_NODE;
        },
        attribute: function ($receiver, name) {
          var tmp$0;
          return (tmp$0 = $receiver.getAttribute(name)) != null ? tmp$0 : '';
        },
        get_head: {value: function ($receiver) {
          return $receiver != null && $receiver.length > 0 ? $receiver.item(0) : null;
        }},
        get_first: {value: function ($receiver) {
          return _.kotlin.dom.get_head($receiver);
        }},
        get_tail: {value: function ($receiver) {
          if ($receiver == null) {
            return null;
          }
           else {
            var s = $receiver.length;
            return s > 0 ? $receiver.item(s - 1) : null;
          }
        }},
        get_last: {value: function ($receiver) {
          return _.kotlin.dom.get_tail($receiver);
        }},
        toXmlString_1: function ($receiver, xmlDeclaration) {
          if (xmlDeclaration === void 0)
            xmlDeclaration = false;
          var tmp$0;
          if ($receiver == null)
            tmp$0 = '';
          else {
            tmp$0 = _.kotlin.dom.nodesToXmlString_lwhwg8$(_.kotlin.dom.toList($receiver), xmlDeclaration);
          }
          return tmp$0;
        },
        nodesToXmlString_lwhwg8$: function (nodes, xmlDeclaration) {
          if (xmlDeclaration === void 0)
            xmlDeclaration = false;
          var builder = new Kotlin.StringBuilder();
          {
            var tmp$0 = nodes.iterator();
            while (tmp$0.hasNext()) {
              var n = tmp$0.next();
              builder.append(_.kotlin.dom.toXmlString_rq0l4m$(n, xmlDeclaration));
            }
          }
          return builder.toString();
        },
        plus_1: function ($receiver, child) {
          if (child != null) {
            $receiver.appendChild(child);
          }
          return $receiver;
        },
        plus: function ($receiver, text) {
          return _.kotlin.dom.addText($receiver, text);
        },
        plusAssign: function ($receiver, text) {
          return _.kotlin.dom.addText($receiver, text);
        },
        createElement: function ($receiver, name, init) {
          var elem = $receiver.createElement(name);
          init.call(elem);
          return elem;
        },
        createElement_1: function ($receiver, name, doc, init) {
          if (doc === void 0)
            doc = null;
          var elem = _.kotlin.dom.ownerDocument($receiver, doc).createElement(name);
          init.call(elem);
          return elem;
        },
        ownerDocument: function ($receiver, doc) {
          if (doc === void 0)
            doc = null;
          var tmp$0;
          if ($receiver.nodeType === Node.DOCUMENT_NODE)
            tmp$0 = $receiver;
          else if (doc == null)
            tmp$0 = $receiver.ownerDocument;
          else
            tmp$0 = doc;
          var answer = tmp$0;
          if (answer == null) {
            throw new Kotlin.IllegalArgumentException('Element does not have an ownerDocument and none was provided for: ' + $receiver);
          }
           else {
            return answer;
          }
        },
        addElement: function ($receiver, name, init) {
          var child = _.kotlin.dom.createElement($receiver, name, init);
          $receiver.appendChild(child);
          return child;
        },
        addElement_1: function ($receiver, name, doc, init) {
          if (doc === void 0)
            doc = null;
          var child = _.kotlin.dom.createElement_1($receiver, name, doc, init);
          $receiver.appendChild(child);
          return child;
        },
        addText: function ($receiver, text, doc) {
          if (doc === void 0)
            doc = null;
          if (text != null) {
            var child = _.kotlin.dom.ownerDocument($receiver, doc).createTextNode(text);
            $receiver.appendChild(child);
          }
          return $receiver;
        }
      }),
      test: Kotlin.definePackage(function () {
        this.asserter = new _.kotlin.test.QUnitAsserter();
      }, /** @lends _.kotlin.test */ {
        todo_n8bj3p$: function (block) {
          Kotlin.println('TODO at ' + block);
        },
        QUnitAsserter: Kotlin.createClass(function () {
          return [_.kotlin.test.Asserter];
        }, null, /** @lends _.kotlin.test.QUnitAsserter.prototype */ {
          assertTrue_ivxn3r$: function (message, actual) {
            ok(actual, message);
          },
          assertEquals_a59ba6$: function (message, expected, actual) {
            ok(Kotlin.equals(expected, actual), message + '. Expected <' + Kotlin.toString(expected) + '> actual <' + Kotlin.toString(actual) + '>');
          },
          assertNotNull_bm4g0d$: function (message, actual) {
            ok(actual != null, message);
          },
          assertNull_bm4g0d$: function (message, actual) {
            ok(actual == null, message);
          },
          fail_61zpoe$: function (message) {
            ok(false, message);
          }
        }),
        assertTrue_2xfrrb$: function (message, block) {
          var actual = block();
          _.kotlin.test.asserter.assertTrue_ivxn3r$(message, actual);
        },
        assertTrue_n8bj3p$: function (block) {
          _.kotlin.test.assertTrue_2xfrrb$('expected true', block);
        },
        assertNot_2xfrrb$f: function (block) {
          return function () {
            return !block();
          };
        },
        assertNot_2xfrrb$: function (message, block) {
          _.kotlin.test.assertTrue_2xfrrb$(message, _.kotlin.test.assertNot_2xfrrb$f(block));
        },
        assertNot_n8bj3p$: function (block) {
          _.kotlin.test.assertNot_2xfrrb$('expected false', block);
        },
        assertTrue_8kj6y5$: function (actual, message) {
          if (message === void 0)
            message = '';
          return _.kotlin.test.assertEquals_8vv676$(true, actual, message);
        },
        assertFalse_8kj6y5$: function (actual, message) {
          if (message === void 0)
            message = '';
          return _.kotlin.test.assertEquals_8vv676$(false, actual, message);
        },
        assertEquals_8vv676$: function (expected, actual, message) {
          if (message === void 0)
            message = '';
          _.kotlin.test.asserter.assertEquals_a59ba6$(message, expected, actual);
        },
        assertNotNull_hwpqgh$: function (actual, message) {
          if (message === void 0)
            message = '';
          _.kotlin.test.asserter.assertNotNull_bm4g0d$(message, actual);
          return actual != null ? actual : Kotlin.throwNPE();
        },
        assertNotNull_74f9dl$: function (actual, block) {
          _.kotlin.test.assertNotNull_ll92s9$(actual, '', block);
        },
        assertNotNull_ll92s9$: function (actual, message, block) {
          _.kotlin.test.asserter.assertNotNull_bm4g0d$(message, actual);
          if (actual != null) {
            block(actual);
          }
        },
        assertNull_hwpqgh$: function (actual, message) {
          if (message === void 0)
            message = '';
          _.kotlin.test.asserter.assertNull_bm4g0d$(message, actual);
        },
        fail_61zpoe$: function (message) {
          if (message === void 0)
            message = '';
          _.kotlin.test.asserter.fail_61zpoe$(message);
        },
        expect_74f9dk$: function (expected, block) {
          _.kotlin.test.expect_ll92sa$(expected, 'expected ' + expected, block);
        },
        expect_ll92sa$: function (expected, message, block) {
          var actual = block();
          _.kotlin.test.assertEquals_8vv676$(expected, actual, message);
        },
        fails_n8bj3p$: function (block) {
          try {
            block();
            _.kotlin.test.asserter.fail_61zpoe$('Expected an exception to be thrown');
            return null;
          }
           catch (e) {
            return e;
          }
        },
        Asserter: Kotlin.createTrait(null)
      }),
      Pair: Kotlin.createClass(null, function (first, second) {
        this.first = first;
        this.second = second;
      }, /** @lends _.kotlin.Pair.prototype */ {
        component1: function () {
          return this.first;
        },
        component2: function () {
          return this.second;
        },
        toString: function () {
          return '(' + this.first + ', ' + this.second + ')';
        }
      }),
      Triple: Kotlin.createClass(null, function (first, second, third) {
        this.first = first;
        this.second = second;
        this.third = third;
      }, /** @lends _.kotlin.Triple.prototype */ {
        component1: function () {
          return this.first;
        },
        component2: function () {
          return this.second;
        },
        component3: function () {
          return this.third;
        },
        toString: function () {
          return '(' + this.first + ', ' + this.second + ', ' + this.third + ')';
        }
      }),
      toString_h3panj$: function ($receiver) {
        return _.kotlin.makeString_mc2pv1$($receiver, ', ', '[', ']');
      },
      mapValues_lh0hhz$: function ($receiver, transform) {
        return _.kotlin.mapValuesTo_7qivbo$($receiver, new Kotlin.ComplexHashMap(), transform);
      },
      iterator_rscjuh$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [Kotlin.Iterator];
        }, null, {
          hasNext: function () {
            return $receiver.hasMoreElements();
          },
          next: function () {
            return $receiver.nextElement();
          }
        });
      },
      iterator_h40uyb$: function ($receiver) {
        return $receiver;
      },
      EmptyIterableException: Kotlin.createClass(function () {
        return [Kotlin.RuntimeException];
      }, function $fun(it) {
        $fun.baseInitializer.call(this, it + ' is empty');
        this.it = it;
      }),
      DuplicateKeyException: Kotlin.createClass(function () {
        return [Kotlin.RuntimeException];
      }, function $fun(message) {
        if (message === void 0)
          message = 'Duplicate keys detected';
        $fun.baseInitializer.call(this, message);
      }),
      get_size: {value: function ($receiver) {
        return $receiver.size();
      }},
      get_empty: {value: function ($receiver) {
        return $receiver.isEmpty();
      }},
      set_f7ra8x$: function ($receiver, key, value) {
        return $receiver.put_wn2jw4$(key, value);
      },
      orEmpty_s8ckw1$: function ($receiver) {
        return $receiver != null ? $receiver : _.kotlin.stdlib_emptyMap();
      },
      contains_6halgi$: function ($receiver, key) {
        return $receiver.containsKey_za3rmp$(key);
      },
      get_key: {value: function ($receiver) {
        return $receiver.getKey();
      }},
      get_value: {value: function ($receiver) {
        return $receiver.getValue();
      }},
      component1: function ($receiver) {
        return $receiver.getKey();
      },
      component2: function ($receiver) {
        return $receiver.getValue();
      },
      getOrElse_9bj33b$: function ($receiver, key, defaultValue) {
        if ($receiver.containsKey_za3rmp$(key)) {
          return $receiver.get_za3rmp$(key);
        }
         else {
          return defaultValue();
        }
      },
      getOrPut_ynyybx$: function ($receiver, key, defaultValue) {
        if ($receiver.containsKey_za3rmp$(key)) {
          return $receiver.get_za3rmp$(key);
        }
         else {
          var answer = defaultValue();
          $receiver.put_wn2jw4$(key, answer);
          return answer;
        }
      },
      iterator_s8ckw1$: function ($receiver) {
        var entrySet = $receiver.entrySet();
        return entrySet.iterator();
      },
      mapValuesTo_7qivbo$: function ($receiver, result, transform) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var e = tmp$0.next();
            var newValue = transform(e);
            result.put_wn2jw4$(_.kotlin.get_key(e), newValue);
          }
        }
        return result;
      },
      putAll_nvpytz$: function ($receiver, values) {
        var tmp$1, tmp$2, tmp$3;
        {
          tmp$1 = values, tmp$2 = tmp$1.length;
          for (var tmp$3 = 0; tmp$3 !== tmp$2; ++tmp$3) {
            var tmp$0 = tmp$1[tmp$3]
            , key = tmp$0.component1()
            , value = tmp$0.component2();
            $receiver.put_wn2jw4$(key, value);
          }
        }
      },
      toMap_cj6vvg$: function ($receiver, map) {
        map.putAll_za3j1t$($receiver);
        return map;
      },
      toMap_uxbsj8$: function ($receiver, map) {
        {
          var tmp$1 = $receiver.iterator();
          while (tmp$1.hasNext()) {
            var tmp$0 = tmp$1.next()
            , key = tmp$0.component1()
            , value = tmp$0.component2();
            if (map.containsKey_za3rmp$(key)) {
              throw new _.kotlin.DuplicateKeyException();
            }
            map.put_wn2jw4$(key, value);
          }
        }
        return map;
      },
      toMap_h3panj$: function ($receiver) {
        return _.kotlin.toMap_uxbsj8$($receiver, new Kotlin.ComplexHashMap());
      },
      mapValues_gld13f$: function ($receiver, transform) {
        return _.kotlin.mapValuesTo_7qivbo$($receiver, new Kotlin.ComplexHashMap(_.kotlin.get_size($receiver)), transform);
      },
      get_lastIndex: {value: function ($receiver) {
        return $receiver.length - 1;
      }},
      get_lastIndex_0: {value: function ($receiver) {
        return $receiver.length - 1;
      }},
      get_lastIndex_1: {value: function ($receiver) {
        return $receiver.length - 1;
      }},
      get_lastIndex_2: {value: function ($receiver) {
        return $receiver.length - 1;
      }},
      get_lastIndex_3: {value: function ($receiver) {
        return $receiver.length - 1;
      }},
      get_lastIndex_4: {value: function ($receiver) {
        return $receiver.length - 1;
      }},
      get_lastIndex_5: {value: function ($receiver) {
        return $receiver.length - 1;
      }},
      get_lastIndex_6: {value: function ($receiver) {
        return $receiver.length - 1;
      }},
      get_lastIndex_7: {value: function ($receiver) {
        return $receiver.length - 1;
      }},
      Stream: Kotlin.createTrait(null),
      streamOf_mzrxf8$: function (elements) {
        return _.kotlin.stream_2hx8bi$(elements);
      },
      FilteringStream: Kotlin.createClass(function () {
        return [_.kotlin.Stream];
      }, function (stream, sendWhen, predicate) {
        if (sendWhen === void 0)
          sendWhen = true;
        this.stream = stream;
        this.sendWhen = sendWhen;
        this.predicate = predicate;
      }, /** @lends _.kotlin.FilteringStream.prototype */ {
        iterator: function () {
          return _.kotlin.FilteringStream.iterator$f(this);
        }
      }, /** @lends _.kotlin.FilteringStream */ {
        iterator$f: function (this$FilteringStream) {
          return Kotlin.createObject(function () {
            return [_.kotlin.support.AbstractIterator];
          }, function $fun() {
            $fun.baseInitializer.call(this);
            this.iterator = this$FilteringStream.stream.iterator();
          }, {
            computeNext: function () {
              while (this.iterator.hasNext()) {
                var item = this.iterator.next();
                if (Kotlin.equals(this$FilteringStream.predicate(item), this$FilteringStream.sendWhen)) {
                  this.setNext_za3rmp$(item);
                  return;
                }
              }
              this.done();
            }
          });
        }
      }),
      TransformingStream: Kotlin.createClass(function () {
        return [_.kotlin.Stream];
      }, function (stream, transformer) {
        this.stream = stream;
        this.transformer = transformer;
      }, /** @lends _.kotlin.TransformingStream.prototype */ {
        iterator: function () {
          return _.kotlin.TransformingStream.iterator$f(this);
        }
      }, /** @lends _.kotlin.TransformingStream */ {
        iterator$f: function (this$TransformingStream) {
          return Kotlin.createObject(function () {
            return [_.kotlin.support.AbstractIterator];
          }, function $fun() {
            $fun.baseInitializer.call(this);
            this.iterator = this$TransformingStream.stream.iterator();
          }, {
            computeNext: function () {
              if (this.iterator.hasNext()) {
                this.setNext_za3rmp$(this$TransformingStream.transformer(this.iterator.next()));
              }
               else {
                this.done();
              }
            }
          });
        }
      }),
      MergingStream: Kotlin.createClass(function () {
        return [_.kotlin.Stream];
      }, function (stream1, stream2, transform) {
        this.stream1 = stream1;
        this.stream2 = stream2;
        this.transform = transform;
      }, /** @lends _.kotlin.MergingStream.prototype */ {
        iterator: function () {
          return _.kotlin.MergingStream.iterator$f(this);
        }
      }, /** @lends _.kotlin.MergingStream */ {
        iterator$f: function (this$MergingStream) {
          return Kotlin.createObject(function () {
            return [_.kotlin.support.AbstractIterator];
          }, function $fun() {
            $fun.baseInitializer.call(this);
            this.iterator1 = this$MergingStream.stream1.iterator();
            this.iterator2 = this$MergingStream.stream2.iterator();
          }, {
            computeNext: function () {
              if (this.iterator1.hasNext() && this.iterator2.hasNext()) {
                this.setNext_za3rmp$(this$MergingStream.transform(this.iterator1.next(), this.iterator2.next()));
              }
               else {
                this.done();
              }
            }
          });
        }
      }),
      FlatteningStream: Kotlin.createClass(function () {
        return [_.kotlin.Stream];
      }, function (stream, transformer) {
        this.stream = stream;
        this.transformer = transformer;
      }, /** @lends _.kotlin.FlatteningStream.prototype */ {
        iterator: function () {
          return _.kotlin.FlatteningStream.iterator$f(this);
        }
      }, /** @lends _.kotlin.FlatteningStream */ {
        iterator$f: function (this$FlatteningStream) {
          return Kotlin.createObject(function () {
            return [_.kotlin.support.AbstractIterator];
          }, function $fun() {
            $fun.baseInitializer.call(this);
            this.iterator = this$FlatteningStream.stream.iterator();
            this.itemIterator = null;
          }, {
            computeNext: function () {
              while (this.itemIterator == null) {
                if (!this.iterator.hasNext()) {
                  this.done();
                  break;
                }
                 else {
                  var element = this.iterator.next();
                  var nextItemIterator = this$FlatteningStream.transformer(element).iterator();
                  if (nextItemIterator.hasNext())
                    this.itemIterator = nextItemIterator;
                }
              }
              var currentItemIterator = this.itemIterator;
              if (currentItemIterator == null) {
                this.done();
              }
               else {
                this.setNext_za3rmp$(currentItemIterator.next());
                if (!currentItemIterator.hasNext())
                  this.itemIterator = null;
              }
            }
          });
        }
      }),
      Multistream: Kotlin.createClass(function () {
        return [_.kotlin.Stream];
      }, function (streams) {
        this.streams = streams;
      }, /** @lends _.kotlin.Multistream.prototype */ {
        iterator: function () {
          return _.kotlin.Multistream.iterator$f(this);
        }
      }, /** @lends _.kotlin.Multistream */ {
        iterator$f: function (this$Multistream) {
          return Kotlin.createObject(function () {
            return [_.kotlin.support.AbstractIterator];
          }, function $fun() {
            $fun.baseInitializer.call(this);
            this.iterator = this$Multistream.streams.iterator();
            this.streamIterator = null;
          }, {
            computeNext: function () {
              while (this.streamIterator == null) {
                if (!this.iterator.hasNext()) {
                  this.done();
                  break;
                }
                 else {
                  var stream = this.iterator.next();
                  var nextStreamIterator = stream.iterator();
                  if (nextStreamIterator.hasNext())
                    this.streamIterator = nextStreamIterator;
                }
              }
              var currentStreamIterator = this.streamIterator;
              if (currentStreamIterator == null) {
                this.done();
              }
               else {
                this.setNext_za3rmp$(currentStreamIterator.next());
                if (!currentStreamIterator.hasNext())
                  this.streamIterator = null;
              }
            }
          });
        }
      }),
      LimitedStream: Kotlin.createClass(function () {
        return [_.kotlin.Stream];
      }, function (stream, stopWhen, predicate) {
        if (stopWhen === void 0)
          stopWhen = true;
        this.stream = stream;
        this.stopWhen = stopWhen;
        this.predicate = predicate;
      }, /** @lends _.kotlin.LimitedStream.prototype */ {
        iterator: function () {
          return _.kotlin.LimitedStream.iterator$f(this);
        }
      }, /** @lends _.kotlin.LimitedStream */ {
        iterator$f: function (this$LimitedStream) {
          return Kotlin.createObject(function () {
            return [_.kotlin.support.AbstractIterator];
          }, function $fun() {
            $fun.baseInitializer.call(this);
            this.iterator = this$LimitedStream.stream.iterator();
          }, {
            computeNext: function () {
              if (!this.iterator.hasNext()) {
                this.done();
              }
               else {
                var item = this.iterator.next();
                if (Kotlin.equals(this$LimitedStream.predicate(item), this$LimitedStream.stopWhen)) {
                  this.done();
                }
                 else {
                  this.setNext_za3rmp$(item);
                }
              }
            }
          });
        }
      }),
      FunctionStream: Kotlin.createClass(function () {
        return [_.kotlin.Stream];
      }, function (producer) {
        this.producer = producer;
      }, /** @lends _.kotlin.FunctionStream.prototype */ {
        iterator: function () {
          return _.kotlin.FunctionStream.iterator$f(this);
        }
      }, /** @lends _.kotlin.FunctionStream */ {
        iterator$f: function (this$FunctionStream) {
          return Kotlin.createObject(function () {
            return [_.kotlin.support.AbstractIterator];
          }, function $fun() {
            $fun.baseInitializer.call(this);
          }, {
            computeNext: function () {
              var item = this$FunctionStream.producer();
              if (item == null) {
                this.done();
              }
               else {
                this.setNext_za3rmp$(item);
              }
            }
          });
        }
      }),
      stream_n8bj3p$: function (nextFunction) {
        return new _.kotlin.FunctionStream(nextFunction);
      },
      stream_74f9dl$: function (initialValue, nextFunction) {
        return _.kotlin.stream_n8bj3p$(_.kotlin.toGenerator_n1mtj3$(nextFunction, initialValue));
      },
      stdlib_emptyListClass: Kotlin.createClass(function () {
        return [_.kotlin.List];
      }, null),
      stdlib_emptyList: function () {
        return _.kotlin.stdlib_emptyList_w9bu57$;
      },
      stdlib_emptyMapClass: Kotlin.createClass(function () {
        return [_.kotlin.Map];
      }, null),
      stdlib_emptyMap: function () {
        return _.kotlin.stdlib_emptyMap_h2vi7z$;
      },
      listOf_mzrxf8$: function (values) {
        return values.length === 0 ? _.kotlin.stdlib_emptyList() : _.kotlin.arrayListOf_mzrxf8$(values);
      },
      listOf: function () {
        return _.kotlin.stdlib_emptyList();
      },
      mapOf_mzrxf8$: function (values) {
        return values.length === 0 ? _.kotlin.stdlib_emptyMap() : _.kotlin.hashMapOf_mzrxf8$(values);
      },
      mapOf: function () {
        return _.kotlin.stdlib_emptyMap();
      },
      arrayListOf_mzrxf8$: function (values) {
        return _.kotlin.toCollection_xpmo5j$(values, new Kotlin.ArrayList(values.length));
      },
      hashSetOf_mzrxf8$: function (values) {
        return _.kotlin.toCollection_xpmo5j$(values, new Kotlin.ComplexHashSet(values.length));
      },
      hashMapOf_mzrxf8$: function (values) {
        var answer = new Kotlin.ComplexHashMap(values.length);
        _.kotlin.putAll_nvpytz$(answer, values);
        return answer;
      },
      get_size_1: {value: function ($receiver) {
        return $receiver.size();
      }},
      get_empty_0: {value: function ($receiver) {
        return $receiver.isEmpty();
      }},
      get_indices: {value: function ($receiver) {
        return new Kotlin.NumberRange(0, _.kotlin.get_size_1($receiver) - 1);
      }},
      get_indices_0: {value: function ($receiver) {
        return new Kotlin.NumberRange(0, $receiver - 1);
      }},
      isNotEmpty_tkvw3h$: function ($receiver) {
        return !$receiver.isEmpty();
      },
      get_notEmpty: {value: function ($receiver) {
        return _.kotlin.isNotEmpty_tkvw3h$($receiver);
      }},
      orEmpty_tkvw3h$: function ($receiver) {
        return $receiver != null ? $receiver : _.kotlin.stdlib_emptyList();
      },
      orEmpty_mtvwn1$: function ($receiver) {
        return $receiver != null ? $receiver : _.kotlin.stdlib_emptyList();
      },
      get_first: {value: function ($receiver) {
        return _.kotlin.get_head($receiver);
      }},
      get_last: {value: function ($receiver) {
        var s = _.kotlin.get_size_1($receiver);
        return s > 0 ? $receiver.get_za3lpa$(s - 1) : null;
      }},
      get_lastIndex_8: {value: function ($receiver) {
        return _.kotlin.get_size_1($receiver) - 1;
      }},
      get_head: {value: function ($receiver) {
        return _.kotlin.isNotEmpty_tkvw3h$($receiver) ? $receiver.get_za3lpa$(0) : null;
      }},
      get_tail: {value: function ($receiver) {
        return _.kotlin.drop_odt3s5$($receiver, 1);
      }},
      addAll_wtmfso$: function ($receiver, iterable) {
        {
          if (Kotlin.isType(iterable, _.kotlin.Collection))
            $receiver.addAll_xeylzf$(iterable);
          else {
            var tmp$0 = iterable.iterator();
            while (tmp$0.hasNext()) {
              var item = tmp$0.next();
              $receiver.add_za3rmp$(item);
            }
          }
        }
      },
      addAll_ngcqne$: function ($receiver, stream) {
        {
          var tmp$0 = stream.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            $receiver.add_za3rmp$(item);
          }
        }
      },
      addAll_jl7u2r$: function ($receiver, array) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = array, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            $receiver.add_za3rmp$(item);
          }
        }
      },
      removeAll_wtmfso$: function ($receiver, iterable) {
        {
          if (Kotlin.isType(iterable, _.kotlin.Collection))
            $receiver.removeAll_xeylzf$(iterable);
          else {
            var tmp$0 = iterable.iterator();
            while (tmp$0.hasNext()) {
              var item = tmp$0.next();
              $receiver.remove_za3rmp$(item);
            }
          }
        }
      },
      removeAll_ngcqne$: function ($receiver, stream) {
        {
          var tmp$0 = stream.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            $receiver.remove_za3rmp$(item);
          }
        }
      },
      removeAll_jl7u2r$: function ($receiver, array) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = array, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            $receiver.remove_za3rmp$(item);
          }
        }
      },
      retainAll_wtmfso$: function ($receiver, iterable) {
        {
          if (Kotlin.isType(iterable, _.kotlin.Collection))
            $receiver.retainAll_xeylzf$(iterable);
          else
            $receiver.retainAll_xeylzf$(_.kotlin.toSet_h3panj$(iterable));
        }
      },
      retainAll_jl7u2r$: function ($receiver, array) {
        $receiver.retainAll_xeylzf$(_.kotlin.toSet_2hx8bi$(array));
      },
      drop_fdw77o$: function ($receiver, n) {
        if (n >= $receiver.length)
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList($receiver.length - n);
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_rz0vgy$: function ($receiver, n) {
        if (n >= $receiver.length)
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList($receiver.length - n);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_ucmip8$: function ($receiver, n) {
        if (n >= $receiver.length)
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList($receiver.length - n);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_cwi0e2$: function ($receiver, n) {
        if (n >= $receiver.length)
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList($receiver.length - n);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_3qx2rv$: function ($receiver, n) {
        if (n >= $receiver.length)
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList($receiver.length - n);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_2e964m$: function ($receiver, n) {
        if (n >= $receiver.length)
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList($receiver.length - n);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_tb5gmf$: function ($receiver, n) {
        if (n >= $receiver.length)
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList($receiver.length - n);
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_x09c4g$: function ($receiver, n) {
        if (n >= $receiver.length)
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList($receiver.length - n);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_7naycm$: function ($receiver, n) {
        if (n >= $receiver.length)
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList($receiver.length - n);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_odt3s5$: function ($receiver, n) {
        if (n >= _.kotlin.get_size_1($receiver))
          return new Kotlin.ArrayList();
        var count = 0;
        var list = new Kotlin.ArrayList(_.kotlin.get_size_1($receiver) - n);
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_eq3vf5$: function ($receiver, n) {
        var count = 0;
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ >= n)
              list.add_za3rmp$(item);
          }
        }
        return list;
      },
      drop_9ip83h$f: function (count, n) {
        return function (it) {
          return count.v++ >= n;
        };
      },
      drop_9ip83h$: function ($receiver, n) {
        var count = {v: 0};
        return new _.kotlin.FilteringStream($receiver, void 0, _.kotlin.drop_9ip83h$f(count, n));
      },
      drop_n7iutu$: function ($receiver, n) {
        return $receiver.substring(Math.min(n, _.kotlin.get_size_0($receiver)));
      },
      dropWhile_de9h66$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_50zxbw$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_x245au$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_h5ed0c$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_24jijj$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_im8pe8$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_1xntkt$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_3cuuyy$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_p67zio$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_vqr6wr$: function ($receiver, predicate) {
        var yielding = false;
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (yielding)
              list.add_za3rmp$(item);
            else if (!predicate(item)) {
              list.add_za3rmp$(item);
              yielding = true;
            }
          }
        }
        return list;
      },
      dropWhile_9fpnal$f: function (yielding, predicate) {
        return function (it) {
          if (yielding.v)
            return true;
          else if (!predicate(it)) {
            yielding.v = true;
            return true;
          }
           else
            return false;
        };
      },
      dropWhile_9fpnal$: function ($receiver, predicate) {
        var yielding = {v: false};
        return new _.kotlin.FilteringStream($receiver, void 0, _.kotlin.dropWhile_9fpnal$f(yielding, predicate));
      },
      dropWhile_t73kuc$: function ($receiver, predicate) {
        var tmp$0;
        {
          tmp$0 = $receiver.length - 1 + 1;
          for (var index = 0; index !== tmp$0; index++)
            if (!predicate($receiver.charAt(index))) {
              return $receiver.substring(index);
            }
        }
        return '';
      },
      filter_de9h66$: function ($receiver, predicate) {
        return _.kotlin.filterTo_1jm7xb$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_50zxbw$: function ($receiver, predicate) {
        return _.kotlin.filterTo_uoz9bj$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_x245au$: function ($receiver, predicate) {
        return _.kotlin.filterTo_o451x3$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_h5ed0c$: function ($receiver, predicate) {
        return _.kotlin.filterTo_xryfpz$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_24jijj$: function ($receiver, predicate) {
        return _.kotlin.filterTo_6s9ff2$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_im8pe8$: function ($receiver, predicate) {
        return _.kotlin.filterTo_lbhsbh$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_1xntkt$: function ($receiver, predicate) {
        return _.kotlin.filterTo_4m2m1i$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_3cuuyy$: function ($receiver, predicate) {
        return _.kotlin.filterTo_ru2r$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_p67zio$: function ($receiver, predicate) {
        return _.kotlin.filterTo_wion7n$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_vqr6wr$: function ($receiver, predicate) {
        return _.kotlin.filterTo_ywx4y6$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_gld13f$: function ($receiver, predicate) {
        return _.kotlin.filterTo_inv7mm$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filter_9fpnal$: function ($receiver, predicate) {
        return new _.kotlin.FilteringStream($receiver, true, predicate);
      },
      filter_t73kuc$: function ($receiver, predicate) {
        return _.kotlin.filterTo_2ngy80$($receiver, new Kotlin.StringBuilder(), predicate).toString();
      },
      filterNot_de9h66$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_1jm7xb$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_50zxbw$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_uoz9bj$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_x245au$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_o451x3$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_h5ed0c$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_xryfpz$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_24jijj$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_6s9ff2$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_im8pe8$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_lbhsbh$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_1xntkt$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_4m2m1i$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_3cuuyy$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_ru2r$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_p67zio$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_wion7n$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_vqr6wr$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_ywx4y6$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_gld13f$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_inv7mm$($receiver, new Kotlin.ArrayList(), predicate);
      },
      filterNot_9fpnal$: function ($receiver, predicate) {
        return new _.kotlin.FilteringStream($receiver, false, predicate);
      },
      filterNot_t73kuc$: function ($receiver, predicate) {
        return _.kotlin.filterNotTo_2ngy80$($receiver, new Kotlin.StringBuilder(), predicate).toString();
      },
      filterNotNull_2hx8bi$: function ($receiver) {
        return _.kotlin.filterNotNullTo_xpmo5j$($receiver, new Kotlin.ArrayList());
      },
      filterNotNull_h3panj$: function ($receiver) {
        return _.kotlin.filterNotNullTo_4jj70a$($receiver, new Kotlin.ArrayList());
      },
      filterNotNull_pdnvbz$f: function (it) {
        return it == null;
      },
      filterNotNull_pdnvbz$: function ($receiver) {
        return new _.kotlin.FilteringStream($receiver, false, _.kotlin.filterNotNull_pdnvbz$f);
      },
      filterNotNullTo_xpmo5j$: function ($receiver, destination) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (element != null)
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotNullTo_4jj70a$: function ($receiver, destination) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (element != null)
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotNullTo_791eew$: function ($receiver, destination) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (element != null)
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_1jm7xb$: function ($receiver, destination, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_uoz9bj$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_o451x3$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_xryfpz$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_6s9ff2$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_lbhsbh$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_4m2m1i$: function ($receiver, destination, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_ru2r$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_wion7n$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_ywx4y6$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_inv7mm$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_ggat1c$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterNotTo_2ngy80$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              destination.append(element);
          }
        }
        return destination;
      },
      filterTo_1jm7xb$: function ($receiver, destination, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_uoz9bj$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_o451x3$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_xryfpz$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_6s9ff2$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_lbhsbh$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_4m2m1i$: function ($receiver, destination, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_ru2r$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_wion7n$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_ywx4y6$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_inv7mm$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_ggat1c$: function ($receiver, destination, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              destination.add_za3rmp$(element);
          }
        }
        return destination;
      },
      filterTo_2ngy80$: function ($receiver, destination, predicate) {
        var tmp$0;
        {
          tmp$0 = $receiver.length - 1 + 1;
          for (var index = 0; index !== tmp$0; index++) {
            var element = $receiver.charAt(index);
            if (predicate(element))
              destination.append(element);
          }
        }
        return destination;
      },
      slice_qxrbi5$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver[index]);
          }
        }
        return list;
      },
      slice_34aosx$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver[index]);
          }
        }
        return list;
      },
      slice_dto1g5$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver[index]);
          }
        }
        return list;
      },
      slice_ldb6x3$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver[index]);
          }
        }
        return list;
      },
      slice_5ya7ho$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver[index]);
          }
        }
        return list;
      },
      slice_t349z9$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver[index]);
          }
        }
        return list;
      },
      slice_3cdrzs$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver[index]);
          }
        }
        return list;
      },
      slice_cc6qan$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver[index]);
          }
        }
        return list;
      },
      slice_w98n8l$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver[index]);
          }
        }
        return list;
      },
      slice_h9kosk$: function ($receiver, indices) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            list.add_za3rmp$($receiver.get_za3lpa$(index));
          }
        }
        return list;
      },
      slice_n9t38v$: function ($receiver, indices) {
        var result = new Kotlin.StringBuilder();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var i = tmp$0.next();
            result.append($receiver.charAt(i));
          }
        }
        return result.toString();
      },
      take_fdw77o$: function ($receiver, n) {
        var count = 0;
        var realN = n > $receiver.length ? $receiver.length : n;
        var list = new Kotlin.ArrayList(realN);
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_rz0vgy$: function ($receiver, n) {
        var count = 0;
        var realN = n > $receiver.length ? $receiver.length : n;
        var list = new Kotlin.ArrayList(realN);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_ucmip8$: function ($receiver, n) {
        var count = 0;
        var realN = n > $receiver.length ? $receiver.length : n;
        var list = new Kotlin.ArrayList(realN);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_cwi0e2$: function ($receiver, n) {
        var count = 0;
        var realN = n > $receiver.length ? $receiver.length : n;
        var list = new Kotlin.ArrayList(realN);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_3qx2rv$: function ($receiver, n) {
        var count = 0;
        var realN = n > $receiver.length ? $receiver.length : n;
        var list = new Kotlin.ArrayList(realN);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_2e964m$: function ($receiver, n) {
        var count = 0;
        var realN = n > $receiver.length ? $receiver.length : n;
        var list = new Kotlin.ArrayList(realN);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_tb5gmf$: function ($receiver, n) {
        var count = 0;
        var realN = n > $receiver.length ? $receiver.length : n;
        var list = new Kotlin.ArrayList(realN);
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_x09c4g$: function ($receiver, n) {
        var count = 0;
        var realN = n > $receiver.length ? $receiver.length : n;
        var list = new Kotlin.ArrayList(realN);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_7naycm$: function ($receiver, n) {
        var count = 0;
        var realN = n > $receiver.length ? $receiver.length : n;
        var list = new Kotlin.ArrayList(realN);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_odt3s5$: function ($receiver, n) {
        var count = 0;
        var realN = n > _.kotlin.get_size_1($receiver) ? _.kotlin.get_size_1($receiver) : n;
        var list = new Kotlin.ArrayList(realN);
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ === realN)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_eq3vf5$: function ($receiver, n) {
        var count = 0;
        var list = new Kotlin.ArrayList(n);
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (count++ === n)
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      take_9ip83h$f: function (count, n) {
        return function (it) {
          return count.v++ === n;
        };
      },
      take_9ip83h$: function ($receiver, n) {
        var count = {v: 0};
        return new _.kotlin.LimitedStream($receiver, void 0, _.kotlin.take_9ip83h$f(count, n));
      },
      take_n7iutu$: function ($receiver, n) {
        return $receiver.substring(0, Math.min(n, _.kotlin.get_size_0($receiver)));
      },
      takeWhile_de9h66$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_50zxbw$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_x245au$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_h5ed0c$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_24jijj$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_im8pe8$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_1xntkt$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_3cuuyy$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_p67zio$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_vqr6wr$: function ($receiver, predicate) {
        var list = new Kotlin.ArrayList();
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (!predicate(item))
              break;
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      takeWhile_9fpnal$: function ($receiver, predicate) {
        return new _.kotlin.LimitedStream($receiver, false, predicate);
      },
      takeWhile_t73kuc$: function ($receiver, predicate) {
        var tmp$0;
        {
          tmp$0 = $receiver.length - 1 + 1;
          for (var index = 0; index !== tmp$0; index++)
            if (!predicate($receiver.charAt(index))) {
              return $receiver.substring(0, index);
            }
        }
        return $receiver;
      },
      stream_2hx8bi$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return Kotlin.arrayIterator($receiver);
          }
        });
      },
      stream_l1lu5s$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return Kotlin.arrayIterator($receiver);
          }
        });
      },
      stream_964n92$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return Kotlin.arrayIterator($receiver);
          }
        });
      },
      stream_355nu0$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return Kotlin.arrayIterator($receiver);
          }
        });
      },
      stream_bvy38t$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return Kotlin.arrayIterator($receiver);
          }
        });
      },
      stream_rjqrz0$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return Kotlin.arrayIterator($receiver);
          }
        });
      },
      stream_tmsbgp$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return Kotlin.arrayIterator($receiver);
          }
        });
      },
      stream_se6h4y$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return Kotlin.arrayIterator($receiver);
          }
        });
      },
      stream_i2lc78$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return Kotlin.arrayIterator($receiver);
          }
        });
      },
      stream_h3panj$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return $receiver.iterator();
          }
        });
      },
      stream_pdnvbz$: function ($receiver) {
        return $receiver;
      },
      stream_pdl1w0$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.Stream];
        }, null, {
          iterator: function () {
            return _.kotlin.iterator_gw00vq$($receiver);
          }
        });
      },
      requireNoNulls_2hx8bi$: function ($receiver) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (element == null) {
              throw new Kotlin.IllegalArgumentException('null element found in ' + $receiver);
            }
          }
        }
        return $receiver;
      },
      requireNoNulls_h3panj$: function ($receiver) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (element == null) {
              throw new Kotlin.IllegalArgumentException('null element found in ' + $receiver);
            }
          }
        }
        return $receiver;
      },
      requireNoNulls_mtvwn1$: function ($receiver) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (element == null) {
              throw new Kotlin.IllegalArgumentException('null element found in ' + $receiver);
            }
          }
        }
        return $receiver;
      },
      requireNoNulls_pdnvbz$f: function (this$requireNoNulls) {
        return function (it) {
          if (it == null) {
            throw new Kotlin.IllegalArgumentException('null element found in ' + this$requireNoNulls);
          }
          return true;
        };
      },
      requireNoNulls_pdnvbz$: function ($receiver) {
        return new _.kotlin.FilteringStream($receiver, void 0, _.kotlin.requireNoNulls_pdnvbz$f($receiver));
      },
      flatMap_de9h66$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_1jm7xb$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_50zxbw$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_uoz9bj$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_x245au$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_o451x3$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_h5ed0c$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_xryfpz$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_24jijj$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_6s9ff2$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_im8pe8$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_lbhsbh$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_1xntkt$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_4m2m1i$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_3cuuyy$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_ru2r$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_p67zio$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_wion7n$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_vqr6wr$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_ywx4y6$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_gld13f$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_inv7mm$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_t73kuc$: function ($receiver, transform) {
        return _.kotlin.flatMapTo_caazm9$($receiver, new Kotlin.ArrayList(), transform);
      },
      flatMap_9fpnal$: function ($receiver, transform) {
        return new _.kotlin.FlatteningStream($receiver, transform);
      },
      flatMapTo_1jm7xb$: function ($receiver, destination, transform) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_uoz9bj$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_o451x3$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_xryfpz$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_6s9ff2$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_lbhsbh$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_4m2m1i$: function ($receiver, destination, transform) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_ru2r$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_wion7n$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_ywx4y6$: function ($receiver, destination, transform) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_inv7mm$: function ($receiver, destination, transform) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_caazm9$: function ($receiver, destination, transform) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_wtmfso$(destination, list);
          }
        }
        return destination;
      },
      flatMapTo_ggat1c$: function ($receiver, destination, transform) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var list = transform(element);
            _.kotlin.addAll_ngcqne$(destination, list);
          }
        }
        return destination;
      },
      groupBy_de9h66$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_dmnozt$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_50zxbw$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_7i5ojf$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_x245au$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_du5x9d$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_h5ed0c$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_4mj9lf$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_24jijj$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_yr676w$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_im8pe8$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_fktjsp$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_1xntkt$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_8qaat0$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_3cuuyy$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_rnq9xv$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_p67zio$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_yb8vhj$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_vqr6wr$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_cyhgqk$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_gld13f$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_7qivbo$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_9fpnal$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_fsw8ae$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupBy_t73kuc$: function ($receiver, toKey) {
        return _.kotlin.groupByTo_16syit$($receiver, new Kotlin.ComplexHashMap(), toKey);
      },
      groupByTo_dmnozt$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_dmnozt$: function ($receiver, map, toKey) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_dmnozt$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_7i5ojf$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_7i5ojf$: function ($receiver, map, toKey) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_7i5ojf$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_du5x9d$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_du5x9d$: function ($receiver, map, toKey) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_du5x9d$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_4mj9lf$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_4mj9lf$: function ($receiver, map, toKey) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_4mj9lf$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_yr676w$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_yr676w$: function ($receiver, map, toKey) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_yr676w$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_fktjsp$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_fktjsp$: function ($receiver, map, toKey) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_fktjsp$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_8qaat0$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_8qaat0$: function ($receiver, map, toKey) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_8qaat0$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_rnq9xv$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_rnq9xv$: function ($receiver, map, toKey) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_rnq9xv$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_yb8vhj$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_yb8vhj$: function ($receiver, map, toKey) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_yb8vhj$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_cyhgqk$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_cyhgqk$: function ($receiver, map, toKey) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_cyhgqk$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_7qivbo$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_7qivbo$: function ($receiver, map, toKey) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_7qivbo$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_fsw8ae$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_fsw8ae$: function ($receiver, map, toKey) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_fsw8ae$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      groupByTo_16syit$f: function () {
        return new Kotlin.ArrayList();
      },
      groupByTo_16syit$: function ($receiver, map, toKey) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            var key = toKey(element);
            var list = _.kotlin.getOrPut_ynyybx$(map, key, _.kotlin.groupByTo_16syit$f);
            list.add_za3rmp$(element);
          }
        }
        return map;
      },
      map_de9h66$: function ($receiver, transform) {
        return _.kotlin.mapTo_1jm7xb$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_50zxbw$: function ($receiver, transform) {
        return _.kotlin.mapTo_uoz9bj$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_x245au$: function ($receiver, transform) {
        return _.kotlin.mapTo_o451x3$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_h5ed0c$: function ($receiver, transform) {
        return _.kotlin.mapTo_xryfpz$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_24jijj$: function ($receiver, transform) {
        return _.kotlin.mapTo_6s9ff2$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_im8pe8$: function ($receiver, transform) {
        return _.kotlin.mapTo_lbhsbh$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_1xntkt$: function ($receiver, transform) {
        return _.kotlin.mapTo_4m2m1i$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_3cuuyy$: function ($receiver, transform) {
        return _.kotlin.mapTo_ru2r$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_p67zio$: function ($receiver, transform) {
        return _.kotlin.mapTo_wion7n$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_vqr6wr$: function ($receiver, transform) {
        return _.kotlin.mapTo_ywx4y6$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_gld13f$: function ($receiver, transform) {
        return _.kotlin.mapTo_inv7mm$($receiver, new Kotlin.ArrayList(), transform);
      },
      map_9fpnal$: function ($receiver, transform) {
        return new _.kotlin.TransformingStream($receiver, transform);
      },
      map_t73kuc$: function ($receiver, transform) {
        return _.kotlin.mapTo_caazm9$($receiver, new Kotlin.ArrayList(), transform);
      },
      mapNotNull_de9h66$: function ($receiver, transform) {
        return _.kotlin.mapNotNullTo_1jm7xb$($receiver, new Kotlin.ArrayList(), transform);
      },
      mapNotNull_vqr6wr$: function ($receiver, transform) {
        return _.kotlin.mapNotNullTo_ywx4y6$($receiver, new Kotlin.ArrayList(), transform);
      },
      mapNotNull_9fpnal$f: function (it) {
        return it == null;
      },
      mapNotNull_9fpnal$: function ($receiver, transform) {
        return new _.kotlin.TransformingStream(new _.kotlin.FilteringStream($receiver, false, _.kotlin.mapNotNull_9fpnal$f), transform);
      },
      mapNotNullTo_1jm7xb$: function ($receiver, destination, transform) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (element != null) {
              destination.add_za3rmp$(transform(element));
            }
          }
        }
        return destination;
      },
      mapNotNullTo_ywx4y6$: function ($receiver, destination, transform) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (element != null) {
              destination.add_za3rmp$(transform(element));
            }
          }
        }
        return destination;
      },
      mapNotNullTo_ggat1c$: function ($receiver, destination, transform) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (element != null) {
              destination.add_za3rmp$(transform(element));
            }
          }
        }
        return destination;
      },
      mapTo_1jm7xb$: function ($receiver, destination, transform) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_uoz9bj$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_o451x3$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_xryfpz$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_6s9ff2$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_lbhsbh$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_4m2m1i$: function ($receiver, destination, transform) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_ru2r$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_wion7n$: function ($receiver, destination, transform) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_ywx4y6$: function ($receiver, destination, transform) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_inv7mm$: function ($receiver, destination, transform) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_ggat1c$: function ($receiver, destination, transform) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      mapTo_caazm9$: function ($receiver, destination, transform) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            destination.add_za3rmp$(transform(item));
          }
        }
        return destination;
      },
      withIndices_2hx8bi$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_2hx8bi$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_1jm7xb$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_2hx8bi$f(index));
      },
      withIndices_l1lu5s$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_l1lu5s$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_uoz9bj$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_l1lu5s$f(index));
      },
      withIndices_964n92$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_964n92$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_o451x3$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_964n92$f(index));
      },
      withIndices_355nu0$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_355nu0$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_xryfpz$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_355nu0$f(index));
      },
      withIndices_bvy38t$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_bvy38t$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_6s9ff2$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_bvy38t$f(index));
      },
      withIndices_rjqrz0$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_rjqrz0$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_lbhsbh$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_rjqrz0$f(index));
      },
      withIndices_tmsbgp$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_tmsbgp$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_4m2m1i$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_tmsbgp$f(index));
      },
      withIndices_se6h4y$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_se6h4y$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_ru2r$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_se6h4y$f(index));
      },
      withIndices_i2lc78$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_i2lc78$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_wion7n$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_i2lc78$f(index));
      },
      withIndices_h3panj$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_h3panj$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_ywx4y6$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_h3panj$f(index));
      },
      withIndices_pdnvbz$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_pdnvbz$: function ($receiver) {
        var index = {v: 0};
        return new _.kotlin.TransformingStream($receiver, _.kotlin.withIndices_pdnvbz$f(index));
      },
      withIndices_pdl1w0$f: function (index) {
        return function (it) {
          return _.kotlin.to_l1ob02$(index.v++, it);
        };
      },
      withIndices_pdl1w0$: function ($receiver) {
        var index = {v: 0};
        return _.kotlin.mapTo_caazm9$($receiver, new Kotlin.ArrayList(), _.kotlin.withIndices_pdl1w0$f(index));
      },
      distinct_2hx8bi$: function ($receiver) {
        return _.kotlin.toMutableSet_2hx8bi$($receiver);
      },
      distinct_l1lu5s$: function ($receiver) {
        return _.kotlin.toMutableSet_l1lu5s$($receiver);
      },
      distinct_964n92$: function ($receiver) {
        return _.kotlin.toMutableSet_964n92$($receiver);
      },
      distinct_355nu0$: function ($receiver) {
        return _.kotlin.toMutableSet_355nu0$($receiver);
      },
      distinct_bvy38t$: function ($receiver) {
        return _.kotlin.toMutableSet_bvy38t$($receiver);
      },
      distinct_rjqrz0$: function ($receiver) {
        return _.kotlin.toMutableSet_rjqrz0$($receiver);
      },
      distinct_tmsbgp$: function ($receiver) {
        return _.kotlin.toMutableSet_tmsbgp$($receiver);
      },
      distinct_se6h4y$: function ($receiver) {
        return _.kotlin.toMutableSet_se6h4y$($receiver);
      },
      distinct_i2lc78$: function ($receiver) {
        return _.kotlin.toMutableSet_i2lc78$($receiver);
      },
      distinct_h3panj$: function ($receiver) {
        return _.kotlin.toMutableSet_h3panj$($receiver);
      },
      intersect_qxrbi5$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_2hx8bi$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      intersect_34aosx$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_l1lu5s$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      intersect_dto1g5$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_964n92$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      intersect_ldb6x3$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_355nu0$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      intersect_5ya7ho$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_bvy38t$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      intersect_t349z9$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_rjqrz0$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      intersect_3cdrzs$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_tmsbgp$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      intersect_cc6qan$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_se6h4y$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      intersect_w98n8l$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_i2lc78$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      intersect_975xw0$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_h3panj$($receiver);
        _.kotlin.retainAll_wtmfso$(set, other);
        return set;
      },
      subtract_qxrbi5$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_2hx8bi$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      subtract_34aosx$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_l1lu5s$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      subtract_dto1g5$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_964n92$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      subtract_ldb6x3$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_355nu0$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      subtract_5ya7ho$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_bvy38t$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      subtract_t349z9$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_rjqrz0$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      subtract_3cdrzs$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_tmsbgp$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      subtract_cc6qan$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_se6h4y$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      subtract_w98n8l$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_i2lc78$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      subtract_975xw0$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_h3panj$($receiver);
        _.kotlin.removeAll_wtmfso$(set, other);
        return set;
      },
      toMutableSet_2hx8bi$: function ($receiver) {
        var set = new Kotlin.LinkedHashSet($receiver.length);
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            set.add_za3rmp$(item);
          }
        }
        return set;
      },
      toMutableSet_l1lu5s$: function ($receiver) {
        var set = new Kotlin.LinkedHashSet($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            set.add_za3rmp$(item);
          }
        }
        return set;
      },
      toMutableSet_964n92$: function ($receiver) {
        var set = new Kotlin.LinkedHashSet($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            set.add_za3rmp$(item);
          }
        }
        return set;
      },
      toMutableSet_355nu0$: function ($receiver) {
        var set = new Kotlin.LinkedHashSet($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            set.add_za3rmp$(item);
          }
        }
        return set;
      },
      toMutableSet_bvy38t$: function ($receiver) {
        var set = new Kotlin.LinkedHashSet($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            set.add_za3rmp$(item);
          }
        }
        return set;
      },
      toMutableSet_rjqrz0$: function ($receiver) {
        var set = new Kotlin.LinkedHashSet($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            set.add_za3rmp$(item);
          }
        }
        return set;
      },
      toMutableSet_tmsbgp$: function ($receiver) {
        var set = new Kotlin.LinkedHashSet($receiver.length);
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            set.add_za3rmp$(item);
          }
        }
        return set;
      },
      toMutableSet_se6h4y$: function ($receiver) {
        var set = new Kotlin.LinkedHashSet($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            set.add_za3rmp$(item);
          }
        }
        return set;
      },
      toMutableSet_i2lc78$: function ($receiver) {
        var set = new Kotlin.LinkedHashSet($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            set.add_za3rmp$(item);
          }
        }
        return set;
      },
      toMutableSet_h3panj$: function ($receiver) {
        var tmp$0;
        if (Kotlin.isType($receiver, _.kotlin.Collection))
          tmp$0 = _.java.util.LinkedHashSet_xeylzf$($receiver);
        else
          tmp$0 = _.kotlin.toCollection_4jj70a$($receiver, new Kotlin.LinkedHashSet());
        return tmp$0;
      },
      union_qxrbi5$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_2hx8bi$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      union_34aosx$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_l1lu5s$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      union_dto1g5$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_964n92$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      union_ldb6x3$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_355nu0$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      union_5ya7ho$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_bvy38t$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      union_t349z9$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_rjqrz0$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      union_3cdrzs$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_tmsbgp$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      union_cc6qan$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_se6h4y$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      union_w98n8l$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_i2lc78$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      union_975xw0$: function ($receiver, other) {
        var set = _.kotlin.toMutableSet_h3panj$($receiver);
        _.kotlin.addAll_wtmfso$(set, other);
        return set;
      },
      f: function (this$toGenerator, nextValue) {
        return function (result) {
          nextValue.v = this$toGenerator(result);
          return result;
        };
      },
      toGenerator_n1mtj3$f: function (nextValue, this$toGenerator) {
        return function () {
          var tmp$0;
          return (tmp$0 = nextValue.v) != null ? _.kotlin.let_j58jph$(tmp$0, _.kotlin.f(this$toGenerator, nextValue)) : null;
        };
      },
      toGenerator_n1mtj3$: function ($receiver, initialValue) {
        var nextValue = {v: initialValue};
        return _.kotlin.toGenerator_n1mtj3$f(nextValue, $receiver);
      },
      to_l1ob02$: function ($receiver, that) {
        return new _.kotlin.Pair($receiver, that);
      },
      run_n8bj3p$: function (f) {
        return f();
      },
      with_rc1ekn$: function (receiver, f) {
        return f.call(receiver);
      },
      let_j58jph$: function ($receiver, f) {
        return f($receiver);
      },
      downTo_9q324c$: function ($receiver, to) {
        return new _.kotlin.ByteProgression($receiver, to, -1);
      },
      downTo_9q3c22$: function ($receiver, to) {
        return new _.kotlin.CharProgression($receiver.toChar(), to, -1);
      },
      downTo_hl85u0$: function ($receiver, to) {
        return new _.kotlin.ShortProgression($receiver, to, -1);
      },
      downTo_y20kcl$: function ($receiver, to) {
        return new Kotlin.NumberProgression($receiver, to, -1);
      },
      downTo_9q98fk$: function ($receiver, to) {
        return new _.kotlin.LongProgression($receiver.toLong(), to, -(1).toLong());
      },
      downTo_he5dns$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver, to, -1);
      },
      downTo_tylosb$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to, -1.0);
      },
      downTo_sd8xje$: function ($receiver, to) {
        return new _.kotlin.CharProgression($receiver, to.toChar(), -1);
      },
      downTo_sd97h4$: function ($receiver, to) {
        return new _.kotlin.CharProgression($receiver, to, -1);
      },
      downTo_radrzu$: function ($receiver, to) {
        return new _.kotlin.ShortProgression($receiver.toShort(), to, -1);
      },
      downTo_v5vllf$: function ($receiver, to) {
        return new Kotlin.NumberProgression($receiver.toInt(), to, -1);
      },
      downTo_sdf3um$: function ($receiver, to) {
        return new _.kotlin.LongProgression($receiver.toLong(), to, -(1).toLong());
      },
      downTo_r3aztm$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver.toFloat(), to, -1);
      },
      downTo_df7tnx$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver.toDouble(), to, -1.0);
      },
      downTo_9r634a$: function ($receiver, to) {
        return new _.kotlin.ShortProgression($receiver, to, -1);
      },
      downTo_9r5t6k$: function ($receiver, to) {
        return new _.kotlin.ShortProgression($receiver, to.toShort(), -1);
      },
      downTo_i0qws2$: function ($receiver, to) {
        return new _.kotlin.ShortProgression($receiver, to, -1);
      },
      downTo_rt69vj$: function ($receiver, to) {
        return new Kotlin.NumberProgression($receiver, to, -1);
      },
      downTo_9qzwt2$: function ($receiver, to) {
        return new _.kotlin.LongProgression($receiver.toLong(), to, -(1).toLong());
      },
      downTo_i7toya$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver, to, -1);
      },
      downTo_2lzxtr$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to, -1.0);
      },
      downTo_2jcion$: function ($receiver, to) {
        return new Kotlin.NumberProgression($receiver, to, -1);
      },
      downTo_2jc8qx$: function ($receiver, to) {
        return new Kotlin.NumberProgression($receiver, to.toInt(), -1);
      },
      downTo_7dmh8l$: function ($receiver, to) {
        return new Kotlin.NumberProgression($receiver, to, -1);
      },
      downTo_rksjo2$: function ($receiver, to) {
        return new Kotlin.NumberProgression($receiver, to, -1);
      },
      downTo_2j6cdf$: function ($receiver, to) {
        return new _.kotlin.LongProgression($receiver.toLong(), to, -(1).toLong());
      },
      downTo_7kp9et$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver, to, -1);
      },
      downTo_mmqya6$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to, -1.0);
      },
      downTo_jzdo0$: function ($receiver, to) {
        return new _.kotlin.LongProgression($receiver, to.toLong(), -(1).toLong());
      },
      downTo_jznlq$: function ($receiver, to) {
        return new _.kotlin.LongProgression($receiver, to.toLong(), -(1).toLong());
      },
      downTo_hgibo4$: function ($receiver, to) {
        return new _.kotlin.LongProgression($receiver, to.toLong(), -(1).toLong());
      },
      downTo_mw85q1$: function ($receiver, to) {
        return new _.kotlin.LongProgression($receiver, to.toLong(), -(1).toLong());
      },
      downTo_k5jz8$: function ($receiver, to) {
        return new _.kotlin.LongProgression($receiver, to, -(1).toLong());
      },
      downTo_h9fjhw$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver.toFloat(), to, -1);
      },
      downTo_y0unuv$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver.toDouble(), to, -1.0);
      },
      downTo_kquaae$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver, to, -1);
      },
      downTo_kquk84$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver, to.toFloat(), -1);
      },
      downTo_433x66$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver, to, -1);
      },
      downTo_jyaijj$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver, to, -1);
      },
      downTo_kr0glm$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver, to.toFloat(), -1);
      },
      downTo_3w14zy$: function ($receiver, to) {
        return new _.kotlin.FloatProgression($receiver, to, -1);
      },
      downTo_mdktgh$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to, -1.0);
      },
      downTo_stl18b$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to, -1.0);
      },
      downTo_stkral$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to.toDouble(), -1.0);
      },
      downTo_u6e7j3$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to, -1.0);
      },
      downTo_aiyy8i$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to, -1.0);
      },
      downTo_steux3$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to.toDouble(), -1.0);
      },
      downTo_tzbfcv$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to, -1.0);
      },
      downTo_541hxq$: function ($receiver, to) {
        return new _.kotlin.DoubleProgression($receiver, to, -1.0);
      },
      merge_91t4nf$: function ($receiver, array, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_zb2wxp$: function ($receiver, array, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_au6o65$: function ($receiver, array, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_resd0r$: function ($receiver, array, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_6lndoa$: function ($receiver, array, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_g5oapj$: function ($receiver, array, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_f32dm2$: function ($receiver, array, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_oi38kv$: function ($receiver, array, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_pn4jvt$: function ($receiver, array, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_tl80ny$: function ($receiver, array, transform) {
        var first = $receiver.iterator();
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_29xg59$: function ($receiver, array, transform) {
        var first = _.kotlin.iterator_gw00vq$($receiver);
        var second = Kotlin.arrayIterator(array);
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_7bg1pg$: function ($receiver, other, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_vzyamu$: function ($receiver, other, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_r76i9w$: function ($receiver, other, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_d5bgvi$: function ($receiver, other, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_d6i5gz$: function ($receiver, other, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_y6emce$: function ($receiver, other, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_k6l5td$: function ($receiver, other, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_ksuah4$: function ($receiver, other, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_eqb4ua$: function ($receiver, other, transform) {
        var first = Kotlin.arrayIterator($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_hqmbqh$: function ($receiver, other, transform) {
        var first = $receiver.iterator();
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_q03f9y$: function ($receiver, other, transform) {
        var first = _.kotlin.iterator_gw00vq$($receiver);
        var second = other.iterator();
        var list = _.kotlin.arrayListOf_mzrxf8$([]);
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(transform(first.next(), second.next()));
        }
        return list;
      },
      merge_28jw99$: function ($receiver, stream, transform) {
        return new _.kotlin.MergingStream($receiver, stream, transform);
      },
      partition_de9h66$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_50zxbw$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_x245au$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_h5ed0c$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_24jijj$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_im8pe8$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_1xntkt$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_3cuuyy$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_p67zio$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_vqr6wr$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_9fpnal$: function ($receiver, predicate) {
        var first = new Kotlin.ArrayList();
        var second = new Kotlin.ArrayList();
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.add_za3rmp$(element);
            }
             else {
              second.add_za3rmp$(element);
            }
          }
        }
        return new _.kotlin.Pair(first, second);
      },
      partition_t73kuc$: function ($receiver, predicate) {
        var first = new Kotlin.StringBuilder();
        var second = new Kotlin.StringBuilder();
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              first.append(element);
            }
             else {
              second.append(element);
            }
          }
        }
        return new _.kotlin.Pair(first.toString(), second.toString());
      },
      plus_bctcxa$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_2hx8bi$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_w5fksc$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_l1lu5s$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_qsh4fe$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_964n92$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_uy8ycc$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_355nu0$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_kvfz4v$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_bvy38t$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_tev20g$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_rjqrz0$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_wgl9xf$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_tmsbgp$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_v0fo6u$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_se6h4y$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_wshjbk$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_i2lc78$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_fnn263$: function ($receiver, array) {
        var answer = _.kotlin.toArrayList_h3panj$($receiver);
        _.kotlin.addAll_jl7u2r$(answer, array);
        return answer;
      },
      plus_qxrbi5$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_2hx8bi$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_34aosx$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_l1lu5s$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_dto1g5$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_964n92$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_ldb6x3$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_355nu0$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_5ya7ho$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_bvy38t$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_t349z9$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_rjqrz0$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_3cdrzs$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_tmsbgp$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_cc6qan$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_se6h4y$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_w98n8l$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_i2lc78$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_975xw0$: function ($receiver, collection) {
        var answer = _.kotlin.toArrayList_h3panj$($receiver);
        _.kotlin.addAll_wtmfso$(answer, collection);
        return answer;
      },
      plus_1lsq3i$: function ($receiver, collection) {
        return new _.kotlin.Multistream(_.kotlin.streamOf_mzrxf8$([$receiver, _.kotlin.stream_h3panj$(collection)]));
      },
      plus_fdw1a9$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_2hx8bi$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_bsmqrv$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_l1lu5s$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_hgt5d7$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_964n92$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_q79yhh$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_355nu0$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_96a6a3$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_bvy38t$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_thi4tv$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_rjqrz0$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_tb5gmf$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_tmsbgp$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_ssilt7$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_se6h4y$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_x27eb7$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_i2lc78$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_eq3phq$: function ($receiver, element) {
        var answer = _.kotlin.toArrayList_h3panj$($receiver);
        answer.add_za3rmp$(element);
        return answer;
      },
      plus_9ipe0w$: function ($receiver, element) {
        return new _.kotlin.Multistream(_.kotlin.streamOf_mzrxf8$([$receiver, _.kotlin.streamOf_mzrxf8$([element])]));
      },
      plus_y4w53o$: function ($receiver, stream) {
        return new _.kotlin.Multistream(_.kotlin.streamOf_mzrxf8$([$receiver, stream]));
      },
      zip_bctcxa$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_bctcxa$: function ($receiver, array) {
        return _.kotlin.merge_91t4nf$($receiver, array, _.kotlin.zip_bctcxa$f);
      },
      zip_w5fksc$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_w5fksc$: function ($receiver, array) {
        return _.kotlin.merge_zb2wxp$($receiver, array, _.kotlin.zip_w5fksc$f);
      },
      zip_qsh4fe$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_qsh4fe$: function ($receiver, array) {
        return _.kotlin.merge_au6o65$($receiver, array, _.kotlin.zip_qsh4fe$f);
      },
      zip_uy8ycc$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_uy8ycc$: function ($receiver, array) {
        return _.kotlin.merge_resd0r$($receiver, array, _.kotlin.zip_uy8ycc$f);
      },
      zip_kvfz4v$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_kvfz4v$: function ($receiver, array) {
        return _.kotlin.merge_6lndoa$($receiver, array, _.kotlin.zip_kvfz4v$f);
      },
      zip_tev20g$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_tev20g$: function ($receiver, array) {
        return _.kotlin.merge_g5oapj$($receiver, array, _.kotlin.zip_tev20g$f);
      },
      zip_wgl9xf$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_wgl9xf$: function ($receiver, array) {
        return _.kotlin.merge_f32dm2$($receiver, array, _.kotlin.zip_wgl9xf$f);
      },
      zip_v0fo6u$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_v0fo6u$: function ($receiver, array) {
        return _.kotlin.merge_oi38kv$($receiver, array, _.kotlin.zip_v0fo6u$f);
      },
      zip_wshjbk$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_wshjbk$: function ($receiver, array) {
        return _.kotlin.merge_pn4jvt$($receiver, array, _.kotlin.zip_wshjbk$f);
      },
      zip_fnn263$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_fnn263$: function ($receiver, array) {
        return _.kotlin.merge_tl80ny$($receiver, array, _.kotlin.zip_fnn263$f);
      },
      zip_ny9o$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_ny9o$: function ($receiver, array) {
        return _.kotlin.merge_29xg59$($receiver, array, _.kotlin.zip_ny9o$f);
      },
      zip_qxrbi5$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_qxrbi5$: function ($receiver, other) {
        return _.kotlin.merge_7bg1pg$($receiver, other, _.kotlin.zip_qxrbi5$f);
      },
      zip_34aosx$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_34aosx$: function ($receiver, other) {
        return _.kotlin.merge_vzyamu$($receiver, other, _.kotlin.zip_34aosx$f);
      },
      zip_dto1g5$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_dto1g5$: function ($receiver, other) {
        return _.kotlin.merge_r76i9w$($receiver, other, _.kotlin.zip_dto1g5$f);
      },
      zip_ldb6x3$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_ldb6x3$: function ($receiver, other) {
        return _.kotlin.merge_d5bgvi$($receiver, other, _.kotlin.zip_ldb6x3$f);
      },
      zip_5ya7ho$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_5ya7ho$: function ($receiver, other) {
        return _.kotlin.merge_d6i5gz$($receiver, other, _.kotlin.zip_5ya7ho$f);
      },
      zip_t349z9$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_t349z9$: function ($receiver, other) {
        return _.kotlin.merge_y6emce$($receiver, other, _.kotlin.zip_t349z9$f);
      },
      zip_3cdrzs$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_3cdrzs$: function ($receiver, other) {
        return _.kotlin.merge_k6l5td$($receiver, other, _.kotlin.zip_3cdrzs$f);
      },
      zip_cc6qan$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_cc6qan$: function ($receiver, other) {
        return _.kotlin.merge_ksuah4$($receiver, other, _.kotlin.zip_cc6qan$f);
      },
      zip_w98n8l$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_w98n8l$: function ($receiver, other) {
        return _.kotlin.merge_eqb4ua$($receiver, other, _.kotlin.zip_w98n8l$f);
      },
      zip_975xw0$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_975xw0$: function ($receiver, other) {
        return _.kotlin.merge_hqmbqh$($receiver, other, _.kotlin.zip_975xw0$f);
      },
      zip_n9t38v$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_n9t38v$: function ($receiver, other) {
        return _.kotlin.merge_q03f9y$($receiver, other, _.kotlin.zip_n9t38v$f);
      },
      zip_94jgcu$: function ($receiver, other) {
        var first = _.kotlin.iterator_gw00vq$($receiver);
        var second = _.kotlin.iterator_gw00vq$(other);
        var list = new Kotlin.ArrayList();
        while (first.hasNext() && second.hasNext()) {
          list.add_za3rmp$(_.kotlin.to_l1ob02$(first.next(), second.next()));
        }
        return list;
      },
      zip_y4w53o$f: function (t1, t2) {
        return _.kotlin.to_l1ob02$(t1, t2);
      },
      zip_y4w53o$: function ($receiver, stream) {
        return new _.kotlin.MergingStream($receiver, stream, _.kotlin.zip_y4w53o$f);
      },
      contains_fdw1a9$: function ($receiver, element) {
        return _.kotlin.indexOf_fdw1a9$($receiver, element) >= 0;
      },
      contains_bsmqrv$: function ($receiver, element) {
        return _.kotlin.indexOf_bsmqrv$($receiver, element) >= 0;
      },
      contains_hgt5d7$: function ($receiver, element) {
        return _.kotlin.indexOf_hgt5d7$($receiver, element) >= 0;
      },
      contains_q79yhh$: function ($receiver, element) {
        return _.kotlin.indexOf_q79yhh$($receiver, element) >= 0;
      },
      contains_96a6a3$: function ($receiver, element) {
        return _.kotlin.indexOf_96a6a3$($receiver, element) >= 0;
      },
      contains_thi4tv$: function ($receiver, element) {
        return _.kotlin.indexOf_thi4tv$($receiver, element) >= 0;
      },
      contains_tb5gmf$: function ($receiver, element) {
        return _.kotlin.indexOf_tb5gmf$($receiver, element) >= 0;
      },
      contains_ssilt7$: function ($receiver, element) {
        return _.kotlin.indexOf_ssilt7$($receiver, element) >= 0;
      },
      contains_x27eb7$: function ($receiver, element) {
        return _.kotlin.indexOf_x27eb7$($receiver, element) >= 0;
      },
      contains_eq3phq$: function ($receiver, element) {
        if (Kotlin.isType($receiver, _.kotlin.Collection))
          return $receiver.contains_za3rmp$(element);
        return _.kotlin.indexOf_eq3phq$($receiver, element) >= 0;
      },
      contains_9ipe0w$: function ($receiver, element) {
        if (Kotlin.isType($receiver, _.kotlin.Collection))
          return $receiver.contains_za3rmp$(element);
        return _.kotlin.indexOf_9ipe0w$($receiver, element) >= 0;
      },
      elementAt_fdw77o$: function ($receiver, index) {
        return $receiver[index];
      },
      elementAt_rz0vgy$: function ($receiver, index) {
        return $receiver[index];
      },
      elementAt_ucmip8$: function ($receiver, index) {
        return $receiver[index];
      },
      elementAt_cwi0e2$: function ($receiver, index) {
        return $receiver[index];
      },
      elementAt_3qx2rv$: function ($receiver, index) {
        return $receiver[index];
      },
      elementAt_2e964m$: function ($receiver, index) {
        return $receiver[index];
      },
      elementAt_tb5gmf$: function ($receiver, index) {
        return $receiver[index];
      },
      elementAt_x09c4g$: function ($receiver, index) {
        return $receiver[index];
      },
      elementAt_7naycm$: function ($receiver, index) {
        return $receiver[index];
      },
      elementAt_eq3vf5$: function ($receiver, index) {
        if (Kotlin.isType($receiver, _.kotlin.List))
          return $receiver.get_za3lpa$(index);
        var iterator = $receiver.iterator();
        var count = 0;
        while (iterator.hasNext()) {
          var element = iterator.next();
          if (index === count++)
            return element;
        }
        throw new RangeError("Collection doesn't contain element at index");
      },
      elementAt_ureun9$: function ($receiver, index) {
        return $receiver.get_za3lpa$(index);
      },
      elementAt_9ip83h$: function ($receiver, index) {
        var iterator = $receiver.iterator();
        var count = 0;
        while (iterator.hasNext()) {
          var element = iterator.next();
          if (index === count++)
            return element;
        }
        throw new RangeError("Collection doesn't contain element at index");
      },
      elementAt_n7iutu$: function ($receiver, index) {
        return $receiver.charAt(index);
      },
      first_2hx8bi$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[0];
      },
      first_l1lu5s$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[0];
      },
      first_964n92$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[0];
      },
      first_355nu0$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[0];
      },
      first_bvy38t$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[0];
      },
      first_rjqrz0$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[0];
      },
      first_tmsbgp$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[0];
      },
      first_se6h4y$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[0];
      },
      first_i2lc78$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[0];
      },
      first_h3panj$: function ($receiver) {
        {
          if (Kotlin.isType($receiver, _.kotlin.List)) {
            if (_.kotlin.get_size_1($receiver) === 0)
              throw new Kotlin.NoSuchElementException('Collection is empty');
            else
              return $receiver.get_za3lpa$(0);
          }
           else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              throw new Kotlin.NoSuchElementException('Collection is empty');
            return iterator.next();
          }
        }
      },
      first_mtvwn1$: function ($receiver) {
        if (_.kotlin.get_size_1($receiver) === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver.get_za3lpa$(0);
      },
      first_pdnvbz$: function ($receiver) {
        {
          if (Kotlin.isType($receiver, _.kotlin.List)) {
            if (_.kotlin.get_size_1($receiver) === 0)
              throw new Kotlin.NoSuchElementException('Collection is empty');
            else
              return $receiver.get_za3lpa$(0);
          }
           else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              throw new Kotlin.NoSuchElementException('Collection is empty');
            return iterator.next();
          }
        }
      },
      first_pdl1w0$: function ($receiver) {
        if (_.kotlin.get_size_0($receiver) === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver.charAt(0);
      },
      first_de9h66$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_50zxbw$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_x245au$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_h5ed0c$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_24jijj$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_im8pe8$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_1xntkt$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_3cuuyy$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_p67zio$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_vqr6wr$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_9fpnal$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      first_t73kuc$: function ($receiver, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        throw new Kotlin.NoSuchElementException('No element matching predicate was found');
      },
      firstOrNull_2hx8bi$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[0] : null;
      },
      firstOrNull_l1lu5s$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[0] : null;
      },
      firstOrNull_964n92$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[0] : null;
      },
      firstOrNull_355nu0$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[0] : null;
      },
      firstOrNull_bvy38t$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[0] : null;
      },
      firstOrNull_rjqrz0$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[0] : null;
      },
      firstOrNull_tmsbgp$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[0] : null;
      },
      firstOrNull_se6h4y$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[0] : null;
      },
      firstOrNull_i2lc78$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[0] : null;
      },
      firstOrNull_h3panj$: function ($receiver) {
        {
          if (Kotlin.isType($receiver, _.kotlin.List)) {
            if (_.kotlin.get_size_1($receiver) === 0)
              return null;
            else
              return $receiver.get_za3lpa$(0);
          }
           else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              return null;
            return iterator.next();
          }
        }
      },
      firstOrNull_mtvwn1$: function ($receiver) {
        return _.kotlin.get_size_1($receiver) > 0 ? $receiver.get_za3lpa$(0) : null;
      },
      firstOrNull_pdnvbz$: function ($receiver) {
        {
          if (Kotlin.isType($receiver, _.kotlin.List)) {
            if (_.kotlin.get_size_1($receiver) === 0)
              return null;
            else
              return $receiver.get_za3lpa$(0);
          }
           else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              return null;
            return iterator.next();
          }
        }
      },
      firstOrNull_pdl1w0$: function ($receiver) {
        return _.kotlin.get_size_0($receiver) > 0 ? $receiver.charAt(0) : null;
      },
      firstOrNull_de9h66$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_50zxbw$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_x245au$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_h5ed0c$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_24jijj$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_im8pe8$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_1xntkt$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_3cuuyy$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_p67zio$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_vqr6wr$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_9fpnal$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      firstOrNull_t73kuc$: function ($receiver, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return element;
          }
        }
        return null;
      },
      indexOf_fdw1a9$: function ($receiver, element) {
        if (element == null) {
          var tmp$0, tmp$1, tmp$2, tmp$3;
          {
            tmp$0 = Kotlin.arrayIndices($receiver), tmp$1 = tmp$0.start, tmp$2 = tmp$0.end, tmp$3 = tmp$0.increment;
            for (var index = tmp$1; index <= tmp$2; index += tmp$3) {
              if ($receiver[index] == null) {
                return index;
              }
            }
          }
        }
         else {
          var tmp$4, tmp$5, tmp$6, tmp$7;
          {
            tmp$4 = Kotlin.arrayIndices($receiver), tmp$5 = tmp$4.start, tmp$6 = tmp$4.end, tmp$7 = tmp$4.increment;
            for (var index_0 = tmp$5; index_0 <= tmp$6; index_0 += tmp$7) {
              if (Kotlin.equals(element, $receiver[index_0])) {
                return index_0;
              }
            }
          }
        }
        return -1;
      },
      indexOf_bsmqrv$: function ($receiver, element) {
        var tmp$0, tmp$1, tmp$2, tmp$3;
        {
          tmp$0 = Kotlin.arrayIndices($receiver), tmp$1 = tmp$0.start, tmp$2 = tmp$0.end, tmp$3 = tmp$0.increment;
          for (var index = tmp$1; index <= tmp$2; index += tmp$3) {
            if (Kotlin.equals(element, $receiver[index])) {
              return index;
            }
          }
        }
        return -1;
      },
      indexOf_hgt5d7$: function ($receiver, element) {
        var tmp$0, tmp$1, tmp$2, tmp$3;
        {
          tmp$0 = Kotlin.arrayIndices($receiver), tmp$1 = tmp$0.start, tmp$2 = tmp$0.end, tmp$3 = tmp$0.increment;
          for (var index = tmp$1; index <= tmp$2; index += tmp$3) {
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      indexOf_q79yhh$: function ($receiver, element) {
        var tmp$0, tmp$1, tmp$2, tmp$3;
        {
          tmp$0 = Kotlin.arrayIndices($receiver), tmp$1 = tmp$0.start, tmp$2 = tmp$0.end, tmp$3 = tmp$0.increment;
          for (var index = tmp$1; index <= tmp$2; index += tmp$3) {
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      indexOf_96a6a3$: function ($receiver, element) {
        var tmp$0, tmp$1, tmp$2, tmp$3;
        {
          tmp$0 = Kotlin.arrayIndices($receiver), tmp$1 = tmp$0.start, tmp$2 = tmp$0.end, tmp$3 = tmp$0.increment;
          for (var index = tmp$1; index <= tmp$2; index += tmp$3) {
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      indexOf_thi4tv$: function ($receiver, element) {
        var tmp$0, tmp$1, tmp$2, tmp$3;
        {
          tmp$0 = Kotlin.arrayIndices($receiver), tmp$1 = tmp$0.start, tmp$2 = tmp$0.end, tmp$3 = tmp$0.increment;
          for (var index = tmp$1; index <= tmp$2; index += tmp$3) {
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      indexOf_tb5gmf$: function ($receiver, element) {
        var tmp$0, tmp$1, tmp$2, tmp$3;
        {
          tmp$0 = Kotlin.arrayIndices($receiver), tmp$1 = tmp$0.start, tmp$2 = tmp$0.end, tmp$3 = tmp$0.increment;
          for (var index = tmp$1; index <= tmp$2; index += tmp$3) {
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      indexOf_ssilt7$: function ($receiver, element) {
        var tmp$0, tmp$1, tmp$2, tmp$3;
        {
          tmp$0 = Kotlin.arrayIndices($receiver), tmp$1 = tmp$0.start, tmp$2 = tmp$0.end, tmp$3 = tmp$0.increment;
          for (var index = tmp$1; index <= tmp$2; index += tmp$3) {
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      indexOf_x27eb7$: function ($receiver, element) {
        var tmp$0, tmp$1, tmp$2, tmp$3;
        {
          tmp$0 = Kotlin.arrayIndices($receiver), tmp$1 = tmp$0.start, tmp$2 = tmp$0.end, tmp$3 = tmp$0.increment;
          for (var index = tmp$1; index <= tmp$2; index += tmp$3) {
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      indexOf_eq3phq$: function ($receiver, element) {
        var index = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (Kotlin.equals(element, item))
              return index;
            index++;
          }
        }
        return -1;
      },
      indexOf_9ipe0w$: function ($receiver, element) {
        var index = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (Kotlin.equals(element, item))
              return index;
            index++;
          }
        }
        return -1;
      },
      last_2hx8bi$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[$receiver.length - 1];
      },
      last_l1lu5s$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[$receiver.length - 1];
      },
      last_964n92$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[$receiver.length - 1];
      },
      last_355nu0$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[$receiver.length - 1];
      },
      last_bvy38t$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[$receiver.length - 1];
      },
      last_rjqrz0$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[$receiver.length - 1];
      },
      last_tmsbgp$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[$receiver.length - 1];
      },
      last_se6h4y$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[$receiver.length - 1];
      },
      last_i2lc78$: function ($receiver) {
        if ($receiver.length === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver[$receiver.length - 1];
      },
      last_h3panj$: function ($receiver) {
        {
          if (Kotlin.isType($receiver, _.kotlin.List)) {
            if (_.kotlin.get_size_1($receiver) === 0)
              throw new Kotlin.NoSuchElementException('Collection is empty');
            else
              return $receiver.get_za3lpa$(_.kotlin.get_size_1($receiver) - 1);
          }
           else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              throw new Kotlin.NoSuchElementException('Collection is empty');
            var last = iterator.next();
            while (iterator.hasNext())
              last = iterator.next();
            return last;
          }
        }
      },
      last_mtvwn1$: function ($receiver) {
        if (_.kotlin.get_size_1($receiver) === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver.get_za3lpa$(_.kotlin.get_size_1($receiver) - 1);
      },
      last_pdnvbz$: function ($receiver) {
        {
          if (Kotlin.isType($receiver, _.kotlin.List)) {
            if (_.kotlin.get_size_1($receiver) === 0)
              throw new Kotlin.NoSuchElementException('Collection is empty');
            else
              return $receiver.get_za3lpa$(_.kotlin.get_size_1($receiver) - 1);
          }
           else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              throw new Kotlin.NoSuchElementException('Collection is empty');
            var last = iterator.next();
            while (iterator.hasNext())
              last = iterator.next();
            return last;
          }
        }
      },
      last_pdl1w0$: function ($receiver) {
        if (_.kotlin.get_size_0($receiver) === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        return $receiver.charAt(_.kotlin.get_size_0($receiver) - 1);
      },
      last_de9h66$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last;
      },
      last_50zxbw$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last != null ? last : Kotlin.throwNPE();
      },
      last_x245au$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last != null ? last : Kotlin.throwNPE();
      },
      last_h5ed0c$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last != null ? last : Kotlin.throwNPE();
      },
      last_24jijj$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last != null ? last : Kotlin.throwNPE();
      },
      last_im8pe8$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last != null ? last : Kotlin.throwNPE();
      },
      last_1xntkt$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last != null ? last : Kotlin.throwNPE();
      },
      last_3cuuyy$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last != null ? last : Kotlin.throwNPE();
      },
      last_p67zio$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last != null ? last : Kotlin.throwNPE();
      },
      last_vqr6wr$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last;
      },
      last_9fpnal$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last;
      },
      last_t73kuc$: function ($receiver, predicate) {
        var last = null;
        var found = false;
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return last != null ? last : Kotlin.throwNPE();
      },
      lastIndexOf_fdw1a9$: function ($receiver, element) {
        if (element == null) {
          {
            var tmp$0 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
            while (tmp$0.hasNext()) {
              var index = tmp$0.next();
              if ($receiver[index] == null) {
                return index;
              }
            }
          }
        }
         else {
          {
            var tmp$1 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
            while (tmp$1.hasNext()) {
              var index_0 = tmp$1.next();
              if (Kotlin.equals(element, $receiver[index_0])) {
                return index_0;
              }
            }
          }
        }
        return -1;
      },
      lastIndexOf_bsmqrv$: function ($receiver, element) {
        {
          var tmp$0 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            if (Kotlin.equals(element, $receiver[index])) {
              return index;
            }
          }
        }
        return -1;
      },
      lastIndexOf_hgt5d7$: function ($receiver, element) {
        {
          var tmp$0 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      lastIndexOf_q79yhh$: function ($receiver, element) {
        {
          var tmp$0 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      lastIndexOf_96a6a3$: function ($receiver, element) {
        {
          var tmp$0 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      lastIndexOf_thi4tv$: function ($receiver, element) {
        {
          var tmp$0 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      lastIndexOf_tb5gmf$: function ($receiver, element) {
        {
          var tmp$0 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      lastIndexOf_ssilt7$: function ($receiver, element) {
        {
          var tmp$0 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      lastIndexOf_x27eb7$: function ($receiver, element) {
        {
          var tmp$0 = _.kotlin.reverse_h3panj$(Kotlin.arrayIndices($receiver)).iterator();
          while (tmp$0.hasNext()) {
            var index = tmp$0.next();
            if (element === $receiver[index]) {
              return index;
            }
          }
        }
        return -1;
      },
      lastIndexOf_eq3phq$: function ($receiver, element) {
        var lastIndex = -1;
        var index = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (Kotlin.equals(element, item))
              lastIndex = index;
            index++;
          }
        }
        return lastIndex;
      },
      lastIndexOf_ureopu$: function ($receiver, element) {
        if (element == null) {
          {
            var tmp$0 = _.kotlin.reverse_h3panj$(_.kotlin.get_indices($receiver)).iterator();
            while (tmp$0.hasNext()) {
              var index = tmp$0.next();
              if ($receiver.get_za3lpa$(index) == null) {
                return index;
              }
            }
          }
        }
         else {
          {
            var tmp$1 = _.kotlin.reverse_h3panj$(_.kotlin.get_indices($receiver)).iterator();
            while (tmp$1.hasNext()) {
              var index_0 = tmp$1.next();
              if (Kotlin.equals(element, $receiver.get_za3lpa$(index_0))) {
                return index_0;
              }
            }
          }
        }
        return -1;
      },
      lastIndexOf_9ipe0w$: function ($receiver, element) {
        var lastIndex = -1;
        var index = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            if (Kotlin.equals(element, item))
              lastIndex = index;
            index++;
          }
        }
        return lastIndex;
      },
      lastOrNull_2hx8bi$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[$receiver.length - 1] : null;
      },
      lastOrNull_l1lu5s$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[$receiver.length - 1] : null;
      },
      lastOrNull_964n92$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[$receiver.length - 1] : null;
      },
      lastOrNull_355nu0$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[$receiver.length - 1] : null;
      },
      lastOrNull_bvy38t$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[$receiver.length - 1] : null;
      },
      lastOrNull_rjqrz0$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[$receiver.length - 1] : null;
      },
      lastOrNull_tmsbgp$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[$receiver.length - 1] : null;
      },
      lastOrNull_se6h4y$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[$receiver.length - 1] : null;
      },
      lastOrNull_i2lc78$: function ($receiver) {
        return $receiver.length > 0 ? $receiver[$receiver.length - 1] : null;
      },
      lastOrNull_h3panj$: function ($receiver) {
        {
          if (Kotlin.isType($receiver, _.kotlin.List))
            return _.kotlin.get_size_1($receiver) > 0 ? $receiver.get_za3lpa$(_.kotlin.get_size_1($receiver) - 1) : null;
          else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              return null;
            var last = iterator.next();
            while (iterator.hasNext())
              last = iterator.next();
            return last;
          }
        }
      },
      lastOrNull_mtvwn1$: function ($receiver) {
        return _.kotlin.get_size_1($receiver) > 0 ? $receiver.get_za3lpa$(_.kotlin.get_size_1($receiver) - 1) : null;
      },
      lastOrNull_pdnvbz$: function ($receiver) {
        {
          if (Kotlin.isType($receiver, _.kotlin.List))
            return _.kotlin.get_size_1($receiver) > 0 ? $receiver.get_za3lpa$(_.kotlin.get_size_1($receiver) - 1) : null;
          else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              return null;
            var last = iterator.next();
            while (iterator.hasNext())
              last = iterator.next();
            return last;
          }
        }
      },
      lastOrNull_pdl1w0$: function ($receiver) {
        return _.kotlin.get_size_0($receiver) > 0 ? $receiver.charAt(_.kotlin.get_size_0($receiver) - 1) : null;
      },
      lastOrNull_de9h66$: function ($receiver, predicate) {
        var last = null;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_50zxbw$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_x245au$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_h5ed0c$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_24jijj$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_im8pe8$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_1xntkt$: function ($receiver, predicate) {
        var last = null;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_3cuuyy$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_p67zio$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_vqr6wr$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_9fpnal$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      lastOrNull_t73kuc$: function ($receiver, predicate) {
        var last = null;
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              last = element;
            }
          }
        }
        return last;
      },
      single_2hx8bi$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_l1lu5s$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_964n92$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_355nu0$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_bvy38t$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_rjqrz0$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_tmsbgp$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_se6h4y$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_i2lc78$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_h3panj$: function ($receiver) {
        var tmp$0 = _.kotlin.get_size_1($receiver), tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver.get_za3lpa$(0);
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        {
          if (Kotlin.isType($receiver, _.kotlin.List))
            return tmp$1;
          else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              throw new Kotlin.NoSuchElementException('Collection is empty');
            var single = iterator.next();
            if (iterator.hasNext())
              throw new Kotlin.IllegalArgumentException('Collection has more than one element');
            return single;
          }
        }
      },
      single_mtvwn1$: function ($receiver) {
        var tmp$0 = _.kotlin.get_size_1($receiver), tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver.get_za3lpa$(0);
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_pdnvbz$: function ($receiver) {
        var tmp$0 = _.kotlin.get_size_1($receiver), tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver.get_za3lpa$(0);
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        {
          if (Kotlin.isType($receiver, _.kotlin.List))
            return tmp$1;
          else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              throw new Kotlin.NoSuchElementException('Collection is empty');
            var single = iterator.next();
            if (iterator.hasNext())
              throw new Kotlin.IllegalArgumentException('Collection has more than one element');
            return single;
          }
        }
      },
      single_pdl1w0$: function ($receiver) {
        var tmp$0 = _.kotlin.get_size_0($receiver), tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver.charAt(0);
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      single_de9h66$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single;
      },
      single_50zxbw$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single != null ? single : Kotlin.throwNPE();
      },
      single_x245au$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single != null ? single : Kotlin.throwNPE();
      },
      single_h5ed0c$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single != null ? single : Kotlin.throwNPE();
      },
      single_24jijj$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single != null ? single : Kotlin.throwNPE();
      },
      single_im8pe8$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single != null ? single : Kotlin.throwNPE();
      },
      single_1xntkt$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single != null ? single : Kotlin.throwNPE();
      },
      single_3cuuyy$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single != null ? single : Kotlin.throwNPE();
      },
      single_p67zio$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single != null ? single : Kotlin.throwNPE();
      },
      single_vqr6wr$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single;
      },
      single_9fpnal$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single;
      },
      single_t73kuc$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          throw new Kotlin.NoSuchElementException("Collection doesn't contain any element matching predicate");
        return single != null ? single : Kotlin.throwNPE();
      },
      singleOrNull_2hx8bi$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_l1lu5s$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_964n92$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_355nu0$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_bvy38t$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_rjqrz0$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_tmsbgp$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_se6h4y$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_i2lc78$: function ($receiver) {
        var tmp$0 = $receiver.length, tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver[0];
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_h3panj$: function ($receiver) {
        var tmp$0 = _.kotlin.get_size_1($receiver), tmp$1;
        if (tmp$0 === 0)
          tmp$1 = null;
        else if (tmp$0 === 1)
          tmp$1 = $receiver.get_za3lpa$(0);
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        {
          if (Kotlin.isType($receiver, _.kotlin.List))
            return tmp$1;
          else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              return null;
            var single = iterator.next();
            if (iterator.hasNext())
              throw new Kotlin.IllegalArgumentException('Collection has more than one element');
            return single;
          }
        }
      },
      singleOrNull_mtvwn1$: function ($receiver) {
        var tmp$0 = _.kotlin.get_size_1($receiver), tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver.get_za3lpa$(0);
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_pdnvbz$: function ($receiver) {
        var tmp$0 = _.kotlin.get_size_1($receiver), tmp$1;
        if (tmp$0 === 0)
          tmp$1 = null;
        else if (tmp$0 === 1)
          tmp$1 = $receiver.get_za3lpa$(0);
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        {
          if (Kotlin.isType($receiver, _.kotlin.List))
            return tmp$1;
          else {
            var iterator = $receiver.iterator();
            if (!iterator.hasNext())
              return null;
            var single = iterator.next();
            if (iterator.hasNext())
              throw new Kotlin.IllegalArgumentException('Collection has more than one element');
            return single;
          }
        }
      },
      singleOrNull_pdl1w0$: function ($receiver) {
        var tmp$0 = _.kotlin.get_size_0($receiver), tmp$1;
        if (tmp$0 === 0)
          throw new Kotlin.NoSuchElementException('Collection is empty');
        else if (tmp$0 === 1)
          tmp$1 = $receiver.charAt(0);
        else
          throw new Kotlin.IllegalArgumentException('Collection has more than one element');
        return tmp$1;
      },
      singleOrNull_de9h66$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_50zxbw$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_x245au$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_h5ed0c$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_24jijj$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_im8pe8$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_1xntkt$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_3cuuyy$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_p67zio$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_vqr6wr$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_9fpnal$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      singleOrNull_t73kuc$: function ($receiver, predicate) {
        var single = null;
        var found = false;
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element)) {
              if (found)
                throw new Kotlin.IllegalArgumentException('Collection contains more than one matching element');
              single = element;
              found = true;
            }
          }
        }
        if (!found)
          return null;
        return single;
      },
      times_97ovpz$: function ($receiver, body) {
        var count = $receiver;
        while (count > 0) {
          body();
          count--;
        }
      },
      require_eltq40$: function (value, message) {
        if (message === void 0)
          message = 'Failed requirement';
        if (!value) {
          throw new Kotlin.IllegalArgumentException(message.toString());
        }
      },
      require_zgzqbg$: function (value, lazyMessage) {
        if (!value) {
          var message = lazyMessage();
          throw new Kotlin.IllegalArgumentException(message.toString());
        }
      },
      requireNotNull_wn2jw4$: function (value, message) {
        if (message === void 0)
          message = 'Required value was null';
        if (value == null) {
          throw new Kotlin.IllegalArgumentException(message.toString());
        }
         else {
          return value;
        }
      },
      check_eltq40$: function (value, message) {
        if (message === void 0)
          message = 'Check failed';
        if (!value) {
          throw new Kotlin.IllegalStateException(message.toString());
        }
      },
      check_zgzqbg$: function (value, lazyMessage) {
        if (!value) {
          var message = lazyMessage();
          throw new Kotlin.IllegalStateException(message.toString());
        }
      },
      checkNotNull_hwpqgh$: function (value, message) {
        if (message === void 0)
          message = 'Required value was null';
        if (value == null) {
          throw new Kotlin.IllegalStateException(message);
        }
         else {
          return value;
        }
      },
      error_61zpoe$: function (message) {
        throw new Kotlin.RuntimeException(message);
      },
      toArrayList_2hx8bi$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toArrayList_l1lu5s$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toArrayList_964n92$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toArrayList_355nu0$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toArrayList_bvy38t$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toArrayList_rjqrz0$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toArrayList_tmsbgp$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toArrayList_se6h4y$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toArrayList_i2lc78$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toArrayList_h3panj$: function ($receiver) {
        return _.kotlin.toCollection_4jj70a$($receiver, new Kotlin.ArrayList());
      },
      toArrayList_pdnvbz$: function ($receiver) {
        return _.kotlin.toCollection_791eew$($receiver, new Kotlin.ArrayList());
      },
      toArrayList_pdl1w0$: function ($receiver) {
        return _.kotlin.toCollection_j1020p$($receiver, new Kotlin.ArrayList());
      },
      toCollection_xpmo5j$: function ($receiver, collection) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_aaeveh$: function ($receiver, collection) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_d1lgh$: function ($receiver, collection) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_ba3pld$: function ($receiver, collection) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_enu0mi$: function ($receiver, collection) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_gk003p$: function ($receiver, collection) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_mglpxq$: function ($receiver, collection) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_vus1ud$: function ($receiver, collection) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_5k8uqj$: function ($receiver, collection) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_4jj70a$: function ($receiver, collection) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_791eew$: function ($receiver, collection) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toCollection_j1020p$: function ($receiver, collection) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            collection.add_za3rmp$(item);
          }
        }
        return collection;
      },
      toHashSet_2hx8bi$: function ($receiver) {
        return _.kotlin.toCollection_xpmo5j$($receiver, new Kotlin.ComplexHashSet());
      },
      toHashSet_l1lu5s$: function ($receiver) {
        return _.kotlin.toCollection_aaeveh$($receiver, new Kotlin.PrimitiveHashSet());
      },
      toHashSet_964n92$: function ($receiver) {
        return _.kotlin.toCollection_d1lgh$($receiver, new Kotlin.PrimitiveHashSet());
      },
      toHashSet_355nu0$: function ($receiver) {
        return _.kotlin.toCollection_ba3pld$($receiver, new Kotlin.PrimitiveHashSet());
      },
      toHashSet_bvy38t$: function ($receiver) {
        return _.kotlin.toCollection_enu0mi$($receiver, new Kotlin.PrimitiveHashSet());
      },
      toHashSet_rjqrz0$: function ($receiver) {
        return _.kotlin.toCollection_gk003p$($receiver, new Kotlin.PrimitiveHashSet());
      },
      toHashSet_tmsbgp$: function ($receiver) {
        return _.kotlin.toCollection_mglpxq$($receiver, new Kotlin.PrimitiveHashSet());
      },
      toHashSet_se6h4y$: function ($receiver) {
        return _.kotlin.toCollection_vus1ud$($receiver, new Kotlin.PrimitiveHashSet());
      },
      toHashSet_i2lc78$: function ($receiver) {
        return _.kotlin.toCollection_5k8uqj$($receiver, new Kotlin.PrimitiveHashSet());
      },
      toHashSet_h3panj$: function ($receiver) {
        return _.kotlin.toCollection_4jj70a$($receiver, new Kotlin.ComplexHashSet());
      },
      toHashSet_pdnvbz$: function ($receiver) {
        return _.kotlin.toCollection_791eew$($receiver, new Kotlin.ComplexHashSet());
      },
      toHashSet_pdl1w0$: function ($receiver) {
        return _.kotlin.toCollection_j1020p$($receiver, new Kotlin.PrimitiveHashSet());
      },
      toLinkedList_2hx8bi$: function ($receiver) {
        return _.kotlin.toCollection_xpmo5j$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_l1lu5s$: function ($receiver) {
        return _.kotlin.toCollection_aaeveh$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_964n92$: function ($receiver) {
        return _.kotlin.toCollection_d1lgh$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_355nu0$: function ($receiver) {
        return _.kotlin.toCollection_ba3pld$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_bvy38t$: function ($receiver) {
        return _.kotlin.toCollection_enu0mi$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_rjqrz0$: function ($receiver) {
        return _.kotlin.toCollection_gk003p$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_tmsbgp$: function ($receiver) {
        return _.kotlin.toCollection_mglpxq$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_se6h4y$: function ($receiver) {
        return _.kotlin.toCollection_vus1ud$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_i2lc78$: function ($receiver) {
        return _.kotlin.toCollection_5k8uqj$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_h3panj$: function ($receiver) {
        return _.kotlin.toCollection_4jj70a$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_pdnvbz$: function ($receiver) {
        return _.kotlin.toCollection_791eew$($receiver, new Kotlin.LinkedList());
      },
      toLinkedList_pdl1w0$: function ($receiver) {
        return _.kotlin.toCollection_j1020p$($receiver, new Kotlin.LinkedList());
      },
      toList_s8ckw1$: function ($receiver) {
        var result = new Kotlin.ArrayList(_.kotlin.get_size($receiver));
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            result.add_za3rmp$(item);
          }
        }
        return result;
      },
      toList_2hx8bi$: function ($receiver) {
        return _.kotlin.toCollection_xpmo5j$($receiver, new Kotlin.ArrayList());
      },
      toList_l1lu5s$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toList_964n92$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toList_355nu0$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toList_bvy38t$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toList_rjqrz0$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toList_tmsbgp$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toList_se6h4y$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toList_i2lc78$: function ($receiver) {
        var list = new Kotlin.ArrayList($receiver.length);
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var item = tmp$0.next();
            list.add_za3rmp$(item);
          }
        }
        return list;
      },
      toList_h3panj$: function ($receiver) {
        return _.kotlin.toCollection_4jj70a$($receiver, new Kotlin.ArrayList());
      },
      toList_pdnvbz$: function ($receiver) {
        return _.kotlin.toCollection_791eew$($receiver, new Kotlin.ArrayList());
      },
      toList_pdl1w0$: function ($receiver) {
        return _.kotlin.toCollection_j1020p$($receiver, new Kotlin.ArrayList());
      },
      toSet_2hx8bi$: function ($receiver) {
        return _.kotlin.toCollection_xpmo5j$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_l1lu5s$: function ($receiver) {
        return _.kotlin.toCollection_aaeveh$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_964n92$: function ($receiver) {
        return _.kotlin.toCollection_d1lgh$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_355nu0$: function ($receiver) {
        return _.kotlin.toCollection_ba3pld$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_bvy38t$: function ($receiver) {
        return _.kotlin.toCollection_enu0mi$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_rjqrz0$: function ($receiver) {
        return _.kotlin.toCollection_gk003p$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_tmsbgp$: function ($receiver) {
        return _.kotlin.toCollection_mglpxq$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_se6h4y$: function ($receiver) {
        return _.kotlin.toCollection_vus1ud$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_i2lc78$: function ($receiver) {
        return _.kotlin.toCollection_5k8uqj$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_h3panj$: function ($receiver) {
        return _.kotlin.toCollection_4jj70a$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_pdnvbz$: function ($receiver) {
        return _.kotlin.toCollection_791eew$($receiver, new Kotlin.LinkedHashSet());
      },
      toSet_pdl1w0$: function ($receiver) {
        return _.kotlin.toCollection_j1020p$($receiver, new Kotlin.LinkedHashSet());
      },
      toSortedSet_2hx8bi$: function ($receiver) {
        return _.kotlin.toCollection_xpmo5j$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_l1lu5s$: function ($receiver) {
        return _.kotlin.toCollection_aaeveh$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_964n92$: function ($receiver) {
        return _.kotlin.toCollection_d1lgh$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_355nu0$: function ($receiver) {
        return _.kotlin.toCollection_ba3pld$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_bvy38t$: function ($receiver) {
        return _.kotlin.toCollection_enu0mi$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_rjqrz0$: function ($receiver) {
        return _.kotlin.toCollection_gk003p$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_tmsbgp$: function ($receiver) {
        return _.kotlin.toCollection_mglpxq$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_se6h4y$: function ($receiver) {
        return _.kotlin.toCollection_vus1ud$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_i2lc78$: function ($receiver) {
        return _.kotlin.toCollection_5k8uqj$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_h3panj$: function ($receiver) {
        return _.kotlin.toCollection_4jj70a$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_pdnvbz$: function ($receiver) {
        return _.kotlin.toCollection_791eew$($receiver, new Kotlin.TreeSet());
      },
      toSortedSet_pdl1w0$: function ($receiver) {
        return _.kotlin.toCollection_j1020p$($receiver, new Kotlin.TreeSet());
      },
      appendString_vt6b28$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_vt6b28$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_v2fgr2$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_v2fgr2$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_ds6lso$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_ds6lso$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_2b34ga$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_2b34ga$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_kjxfqn$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_kjxfqn$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_bt92bi$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_bt92bi$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_xc3j4b$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_xc3j4b$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_2bqqsc$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_2bqqsc$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_ex638e$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_ex638e$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_4ybsr7$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_4ybsr7$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      appendString_tsa3bz$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        _.kotlin.joinTo_tsa3bz$($receiver, buffer, separator, prefix, postfix, limit, truncated);
      },
      joinTo_vt6b28$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element == null ? 'null' : element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_v2fgr2$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_ds6lso$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_2b34ga$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_kjxfqn$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_bt92bi$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_xc3j4b$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_2bqqsc$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_ex638e$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_4ybsr7$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element == null ? 'null' : element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinTo_tsa3bz$: function ($receiver, buffer, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        buffer.append(prefix);
        var count = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (++count > 1)
              buffer.append(separator);
            if (limit < 0 || count <= limit) {
              var text = element == null ? 'null' : element.toString();
              buffer.append(text);
            }
             else
              break;
          }
        }
        if (limit >= 0 && count > limit)
          buffer.append(truncated);
        buffer.append(postfix);
        return buffer;
      },
      joinToString_7s66u8$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_vt6b28$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_cmivou$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_v2fgr2$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_7gqm6g$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_ds6lso$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_5g9kba$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_2b34ga$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_fwx41b$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_kjxfqn$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_sfhf6m$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_bt92bi$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_6b4cej$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_xc3j4b$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_s6c98k$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_2bqqsc$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_pukide$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_ex638e$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_mc2pv1$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_4ybsr7$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      joinToString_tpghi9$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinTo_tsa3bz$($receiver, new Kotlin.StringBuilder(), separator, prefix, postfix, limit, truncated).toString();
      },
      makeString_7s66u8$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_7s66u8$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_cmivou$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_cmivou$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_7gqm6g$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_7gqm6g$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_5g9kba$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_5g9kba$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_fwx41b$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_fwx41b$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_sfhf6m$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_sfhf6m$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_6b4cej$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_6b4cej$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_s6c98k$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_s6c98k$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_pukide$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_pukide$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_mc2pv1$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_mc2pv1$($receiver, separator, prefix, postfix, limit, truncated);
      },
      makeString_tpghi9$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_tpghi9$($receiver, separator, prefix, postfix, limit, truncated);
      },
      trim_94jgcu$: function ($receiver, text) {
        return _.kotlin.trimTrailing_94jgcu$(_.kotlin.trimLeading_94jgcu$($receiver, text), text);
      },
      trim_ex0kps$: function ($receiver, prefix, postfix) {
        return _.kotlin.trimTrailing_94jgcu$(_.kotlin.trimLeading_94jgcu$($receiver, prefix), postfix);
      },
      trimLeading_94jgcu$: function ($receiver, prefix) {
        var answer = $receiver;
        if (answer.startsWith(prefix)) {
          answer = answer.substring(prefix.length);
        }
        return answer;
      },
      trimTrailing_94jgcu$: function ($receiver, postfix) {
        var answer = $receiver;
        if (answer.endsWith(postfix)) {
          answer = answer.substring(0, $receiver.length - postfix.length);
        }
        return answer;
      },
      isNotEmpty_pdl1w0$: function ($receiver) {
        return $receiver != null && $receiver.length > 0;
      },
      iterator_gw00vq$: function ($receiver) {
        return Kotlin.createObject(function () {
          return [_.kotlin.CharIterator];
        }, function $fun() {
          $fun.baseInitializer.call(this);
          this.index_xuly00$ = 0;
        }, {
          nextChar: function () {
            return $receiver.get_za3lpa$(this.index_xuly00$++);
          },
          hasNext: function () {
            return this.index_xuly00$ < $receiver.length;
          }
        });
      },
      orEmpty_pdl1w0$: function ($receiver) {
        return $receiver != null ? $receiver : '';
      },
      get_size_2: {value: function ($receiver) {
        return $receiver.length;
      }},
      get_size_0: {value: function ($receiver) {
        return $receiver.length;
      }},
      get_indices_1: {value: function ($receiver) {
        return new Kotlin.NumberRange(0, $receiver.length - 1);
      }},
      slice_bchp91$: function ($receiver, indices) {
        var sb = new Kotlin.StringBuilder();
        {
          var tmp$0 = indices.iterator();
          while (tmp$0.hasNext()) {
            var i = tmp$0.next();
            sb.append($receiver.get_za3lpa$(i));
          }
        }
        return sb.toString();
      },
      substring_cumll7$: function ($receiver, range) {
        return $receiver.substring(range.start, range.end + 1);
      },
      join_mc2pv1$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_mc2pv1$($receiver, separator, prefix, postfix, limit, truncated);
      },
      join_7s66u8$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_7s66u8$($receiver, separator, prefix, postfix, limit, truncated);
      },
      join_tpghi9$: function ($receiver, separator, prefix, postfix, limit, truncated) {
        if (separator === void 0)
          separator = ', ';
        if (prefix === void 0)
          prefix = '';
        if (postfix === void 0)
          postfix = '';
        if (limit === void 0)
          limit = -1;
        if (truncated === void 0)
          truncated = '...';
        return _.kotlin.joinToString_tpghi9$($receiver, separator, prefix, postfix, limit, truncated);
      },
      substringBefore_960177$: function ($receiver, delimiter) {
        var index = _.js.indexOf_960177$($receiver, delimiter);
        return index === -1 ? $receiver : $receiver.substring(0, index);
      },
      substringBefore_94jgcu$: function ($receiver, delimiter) {
        var index = $receiver.indexOf(delimiter);
        return index === -1 ? $receiver : $receiver.substring(0, index);
      },
      substringAfter_960177$: function ($receiver, delimiter) {
        var index = _.js.indexOf_960177$($receiver, delimiter);
        return index === -1 ? '' : $receiver.substring(index + 1, $receiver.length);
      },
      substringAfter_94jgcu$: function ($receiver, delimiter) {
        var index = $receiver.indexOf(delimiter);
        return index === -1 ? '' : $receiver.substring(index + delimiter.length, $receiver.length);
      },
      substringBeforeLast_960177$: function ($receiver, delimiter) {
        var index = _.js.lastIndexOf_960177$($receiver, delimiter);
        return index === -1 ? $receiver : $receiver.substring(0, index);
      },
      substringBeforeLast_94jgcu$: function ($receiver, delimiter) {
        var index = $receiver.lastIndexOf(delimiter);
        return index === -1 ? $receiver : $receiver.substring(0, index);
      },
      substringAfterLast_960177$: function ($receiver, delimiter) {
        var index = _.js.lastIndexOf_960177$($receiver, delimiter);
        return index === -1 ? '' : $receiver.substring(index + 1, $receiver.length);
      },
      substringAfterLast_94jgcu$: function ($receiver, delimiter) {
        var index = $receiver.lastIndexOf(delimiter);
        return index === -1 ? '' : $receiver.substring(index + delimiter.length, $receiver.length);
      },
      replaceRange_d9884y$: function ($receiver, firstIndex, lastIndex, replacement) {
        if (lastIndex < firstIndex)
          throw new RangeError('Last index (' + lastIndex + ') is less than first index (' + firstIndex + ')');
        var sb = new Kotlin.StringBuilder();
        sb.append($receiver, 0, firstIndex);
        sb.append(replacement);
        sb.append($receiver, lastIndex, $receiver.length);
        return sb.toString();
      },
      replaceRange_rxpzkz$: function ($receiver, range, replacement) {
        if (range.end < range.start)
          throw new RangeError('Last index (' + range.start + ') is less than first index (' + range.end + ')');
        var sb = new Kotlin.StringBuilder();
        sb.append($receiver, 0, range.start);
        sb.append(replacement);
        sb.append($receiver, range.end, $receiver.length);
        return sb.toString();
      },
      replaceBefore_7uhrl1$: function ($receiver, delimiter, replacement) {
        var index = _.js.indexOf_960177$($receiver, delimiter);
        return index === -1 ? replacement : _.kotlin.replaceRange_d9884y$($receiver, 0, index, replacement);
      },
      replaceBefore_ex0kps$: function ($receiver, delimiter, replacement) {
        var index = $receiver.indexOf(delimiter);
        return index === -1 ? replacement : _.kotlin.replaceRange_d9884y$($receiver, 0, index, replacement);
      },
      replaceAfter_7uhrl1$: function ($receiver, delimiter, replacement) {
        var index = _.js.indexOf_960177$($receiver, delimiter);
        return index === -1 ? $receiver : _.kotlin.replaceRange_d9884y$($receiver, index + 1, $receiver.length, replacement);
      },
      replaceAfter_ex0kps$: function ($receiver, delimiter, replacement) {
        var index = $receiver.indexOf(delimiter);
        return index === -1 ? $receiver : _.kotlin.replaceRange_d9884y$($receiver, index + delimiter.length, $receiver.length, replacement);
      },
      replaceAfterLast_ex0kps$: function ($receiver, delimiter, replacement) {
        var index = $receiver.lastIndexOf(delimiter);
        return index === -1 ? $receiver : _.kotlin.replaceRange_d9884y$($receiver, index + delimiter.length, $receiver.length, replacement);
      },
      replaceAfterLast_7uhrl1$: function ($receiver, delimiter, replacement) {
        var index = _.js.lastIndexOf_960177$($receiver, delimiter);
        return index === -1 ? $receiver : _.kotlin.replaceRange_d9884y$($receiver, index + 1, $receiver.length, replacement);
      },
      replaceBeforeLast_7uhrl1$: function ($receiver, delimiter, replacement) {
        var index = _.js.lastIndexOf_960177$($receiver, delimiter);
        return index === -1 ? replacement : _.kotlin.replaceRange_d9884y$($receiver, 0, index, replacement);
      },
      replaceBeforeLast_ex0kps$: function ($receiver, delimiter, replacement) {
        var index = $receiver.lastIndexOf(delimiter);
        return index === -1 ? replacement : _.kotlin.replaceRange_d9884y$($receiver, 0, index, replacement);
      },
      StringBuilder_lxq41y$: function (body) {
        var sb = new Kotlin.StringBuilder();
        body.call(sb);
        return sb;
      },
      append_d4iu1a$: function ($receiver, value) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = value, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            $receiver.append(item);
          }
        }
        return $receiver;
      },
      append_ya45mk$: function ($receiver, value) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = value, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            $receiver.append(item);
          }
        }
        return $receiver;
      },
      append_ya45mk$_0: function ($receiver, value) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = value, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var item = tmp$0[tmp$2];
            $receiver.append(item);
          }
        }
        return $receiver;
      },
      sum_h3panj$: function ($receiver) {
        var iterator = $receiver.iterator();
        var sum = 0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_h3panj$_0: function ($receiver) {
        var iterator = $receiver.iterator();
        var sum = 0.0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_h3panj$_1: function ($receiver) {
        var iterator = $receiver.iterator();
        var sum = 0.0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_h3panj$_2: function ($receiver) {
        var iterator = $receiver.iterator();
        var sum = 0.0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_2hx8bi$: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_tmsbgp$: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_2hx8bi$_0: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0.0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_se6h4y$: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0.0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_2hx8bi$_1: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_964n92$: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_2hx8bi$_2: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_i2lc78$: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_2hx8bi$_3: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0.0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_bvy38t$: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0.0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_2hx8bi$_4: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0.0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      sum_rjqrz0$: function ($receiver) {
        var iterator = Kotlin.arrayIterator($receiver);
        var sum = 0.0;
        while (iterator.hasNext()) {
          sum += iterator.next();
        }
        return sum;
      },
      reverse_2hx8bi$: function ($receiver) {
        var list = _.kotlin.toArrayList_2hx8bi$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_l1lu5s$: function ($receiver) {
        var list = _.kotlin.toArrayList_l1lu5s$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_964n92$: function ($receiver) {
        var list = _.kotlin.toArrayList_964n92$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_355nu0$: function ($receiver) {
        var list = _.kotlin.toArrayList_355nu0$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_bvy38t$: function ($receiver) {
        var list = _.kotlin.toArrayList_bvy38t$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_rjqrz0$: function ($receiver) {
        var list = _.kotlin.toArrayList_rjqrz0$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_tmsbgp$: function ($receiver) {
        var list = _.kotlin.toArrayList_tmsbgp$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_se6h4y$: function ($receiver) {
        var list = _.kotlin.toArrayList_se6h4y$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_i2lc78$: function ($receiver) {
        var list = _.kotlin.toArrayList_i2lc78$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_h3panj$: function ($receiver) {
        var list = _.kotlin.toArrayList_h3panj$($receiver);
        Kotlin.reverse(list);
        return list;
      },
      reverse_pdl1w0$: function ($receiver) {
        return (new Kotlin.StringBuilder()).append($receiver).reverse().toString();
      },
      sort_h3panj$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_h3panj$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      sortBy_lykrt4$: function ($receiver, comparator) {
        var sortedList = _.kotlin.toArrayList_2hx8bi$($receiver);
        Kotlin.collectionsSort(sortedList, comparator);
        return sortedList;
      },
      sortBy_yknd17$: function ($receiver, comparator) {
        var sortedList = _.kotlin.toArrayList_h3panj$($receiver);
        Kotlin.collectionsSort(sortedList, comparator);
        return sortedList;
      },
      sortBy_de9h66$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      sortBy_de9h66$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_2hx8bi$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.sortBy_de9h66$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      sortBy_vqr6wr$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      sortBy_vqr6wr$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_h3panj$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.sortBy_vqr6wr$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      sortDescending_h3panj$f: function (x, y) {
        return -x.compareTo_za3rmp$(y);
      },
      sortDescending_h3panj$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_h3panj$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.sortDescending_h3panj$f);
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      sortDescendingBy_de9h66$f: function (order) {
        return function (x, y) {
          return -order(x).compareTo_za3rmp$(order(y));
        };
      },
      sortDescendingBy_de9h66$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_2hx8bi$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.sortDescendingBy_de9h66$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      sortDescendingBy_vqr6wr$f: function (order) {
        return function (x, y) {
          return -order(x).compareTo_za3rmp$(order(y));
        };
      },
      sortDescendingBy_vqr6wr$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_h3panj$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.sortDescendingBy_vqr6wr$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedList_2hx8bi$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_2hx8bi$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_l1lu5s$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_l1lu5s$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_964n92$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_964n92$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_355nu0$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_355nu0$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_bvy38t$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_bvy38t$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_rjqrz0$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_rjqrz0$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_tmsbgp$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_tmsbgp$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_se6h4y$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_se6h4y$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_i2lc78$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_i2lc78$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_h3panj$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_h3panj$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedList_pdnvbz$: function ($receiver) {
        var sortedList = _.kotlin.toArrayList_pdnvbz$($receiver);
        Kotlin.collectionsSort(sortedList);
        return sortedList;
      },
      toSortedListBy_de9h66$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_de9h66$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_2hx8bi$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_de9h66$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_50zxbw$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_50zxbw$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_l1lu5s$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_50zxbw$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_x245au$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_x245au$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_964n92$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_x245au$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_h5ed0c$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_h5ed0c$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_355nu0$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_h5ed0c$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_24jijj$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_24jijj$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_bvy38t$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_24jijj$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_im8pe8$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_im8pe8$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_rjqrz0$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_im8pe8$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_1xntkt$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_1xntkt$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_tmsbgp$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_1xntkt$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_3cuuyy$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_3cuuyy$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_se6h4y$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_3cuuyy$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_p67zio$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_p67zio$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_i2lc78$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_p67zio$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_vqr6wr$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_vqr6wr$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_h3panj$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_vqr6wr$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      toSortedListBy_9fpnal$f: function (order) {
        return function (x, y) {
          return order(x).compareTo_za3rmp$(order(y));
        };
      },
      toSortedListBy_9fpnal$: function ($receiver, order) {
        var sortedList = _.kotlin.toArrayList_pdnvbz$($receiver);
        var sortBy = Kotlin.comparator(_.kotlin.toSortedListBy_9fpnal$f(order));
        Kotlin.collectionsSort(sortedList, sortBy);
        return sortedList;
      },
      isEmpty_2hx8bi$: function ($receiver) {
        return $receiver.length === 0;
      },
      isEmpty_l1lu5s$: function ($receiver) {
        return $receiver.length === 0;
      },
      isEmpty_964n92$: function ($receiver) {
        return $receiver.length === 0;
      },
      isEmpty_355nu0$: function ($receiver) {
        return $receiver.length === 0;
      },
      isEmpty_bvy38t$: function ($receiver) {
        return $receiver.length === 0;
      },
      isEmpty_rjqrz0$: function ($receiver) {
        return $receiver.length === 0;
      },
      isEmpty_tmsbgp$: function ($receiver) {
        return $receiver.length === 0;
      },
      isEmpty_se6h4y$: function ($receiver) {
        return $receiver.length === 0;
      },
      isEmpty_i2lc78$: function ($receiver) {
        return $receiver.length === 0;
      },
      isNotEmpty_2hx8bi$: function ($receiver) {
        return !_.kotlin.isEmpty_2hx8bi$($receiver);
      },
      isNotEmpty_l1lu5s$: function ($receiver) {
        return !_.kotlin.isEmpty_l1lu5s$($receiver);
      },
      isNotEmpty_964n92$: function ($receiver) {
        return !_.kotlin.isEmpty_964n92$($receiver);
      },
      isNotEmpty_355nu0$: function ($receiver) {
        return !_.kotlin.isEmpty_355nu0$($receiver);
      },
      isNotEmpty_bvy38t$: function ($receiver) {
        return !_.kotlin.isEmpty_bvy38t$($receiver);
      },
      isNotEmpty_rjqrz0$: function ($receiver) {
        return !_.kotlin.isEmpty_rjqrz0$($receiver);
      },
      isNotEmpty_tmsbgp$: function ($receiver) {
        return !_.kotlin.isEmpty_tmsbgp$($receiver);
      },
      isNotEmpty_se6h4y$: function ($receiver) {
        return !_.kotlin.isEmpty_se6h4y$($receiver);
      },
      isNotEmpty_i2lc78$: function ($receiver) {
        return !_.kotlin.isEmpty_i2lc78$($receiver);
      },
      all_de9h66$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_50zxbw$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_x245au$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_h5ed0c$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_24jijj$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_im8pe8$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_1xntkt$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_3cuuyy$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_p67zio$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_vqr6wr$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_gld13f$: function ($receiver, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_9fpnal$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      all_t73kuc$: function ($receiver, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (!predicate(element))
              return false;
          }
        }
        return true;
      },
      any_2hx8bi$: function ($receiver) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            return true;
          }
        }
        return false;
      },
      any_l1lu5s$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_964n92$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_355nu0$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_bvy38t$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_rjqrz0$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_tmsbgp$: function ($receiver) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            return true;
          }
        }
        return false;
      },
      any_se6h4y$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_i2lc78$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_h3panj$: function ($receiver) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_s8ckw1$: function ($receiver) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_pdnvbz$: function ($receiver) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_pdl1w0$: function ($receiver) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return true;
          }
        }
        return false;
      },
      any_de9h66$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_50zxbw$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_x245au$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_h5ed0c$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_24jijj$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_im8pe8$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_1xntkt$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_3cuuyy$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_p67zio$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_vqr6wr$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_gld13f$: function ($receiver, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_9fpnal$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      any_t73kuc$: function ($receiver, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return true;
          }
        }
        return false;
      },
      count_2hx8bi$: function ($receiver) {
        return $receiver.length;
      },
      count_l1lu5s$: function ($receiver) {
        return $receiver.length;
      },
      count_964n92$: function ($receiver) {
        return $receiver.length;
      },
      count_355nu0$: function ($receiver) {
        return $receiver.length;
      },
      count_bvy38t$: function ($receiver) {
        return $receiver.length;
      },
      count_rjqrz0$: function ($receiver) {
        return $receiver.length;
      },
      count_tmsbgp$: function ($receiver) {
        return $receiver.length;
      },
      count_se6h4y$: function ($receiver) {
        return $receiver.length;
      },
      count_i2lc78$: function ($receiver) {
        return $receiver.length;
      },
      count_tkvw3h$: function ($receiver) {
        return _.kotlin.get_size_1($receiver);
      },
      count_h3panj$: function ($receiver) {
        var count = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            count++;
          }
        }
        return count;
      },
      count_s8ckw1$: function ($receiver) {
        return _.kotlin.get_size($receiver);
      },
      count_pdnvbz$: function ($receiver) {
        var count = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            count++;
          }
        }
        return count;
      },
      count_pdl1w0$: function ($receiver) {
        return _.kotlin.get_size_0($receiver);
      },
      count_de9h66$: function ($receiver, predicate) {
        var count = 0;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_50zxbw$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_x245au$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_h5ed0c$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_24jijj$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_im8pe8$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_1xntkt$: function ($receiver, predicate) {
        var count = 0;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_3cuuyy$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_p67zio$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_vqr6wr$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_gld13f$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_9fpnal$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      count_t73kuc$: function ($receiver, predicate) {
        var count = 0;
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              count++;
          }
        }
        return count;
      },
      fold_8stajs$: function ($receiver, initial, operation) {
        var accumulator = initial;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_v8qmra$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_4lvz2o$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_gtjzry$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_pn2g5j$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_tj8q8m$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_s4q4mb$: function ($receiver, initial, operation) {
        var accumulator = initial;
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_g9t0ho$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_8hjqyy$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_gu2wyd$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_9hsf09$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      fold_xn4ira$: function ($receiver, initial, operation) {
        var accumulator = initial;
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            accumulator = operation(accumulator, element);
          }
        }
        return accumulator;
      },
      foldRight_8stajs$: function ($receiver, initial, operation) {
        var index = $receiver.length - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      foldRight_v8qmra$: function ($receiver, initial, operation) {
        var index = $receiver.length - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      foldRight_4lvz2o$: function ($receiver, initial, operation) {
        var index = $receiver.length - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      foldRight_gtjzry$: function ($receiver, initial, operation) {
        var index = $receiver.length - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      foldRight_pn2g5j$: function ($receiver, initial, operation) {
        var index = $receiver.length - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      foldRight_tj8q8m$: function ($receiver, initial, operation) {
        var index = $receiver.length - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      foldRight_s4q4mb$: function ($receiver, initial, operation) {
        var index = $receiver.length - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      foldRight_g9t0ho$: function ($receiver, initial, operation) {
        var index = $receiver.length - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      foldRight_8hjqyy$: function ($receiver, initial, operation) {
        var index = $receiver.length - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      foldRight_qwc90p$: function ($receiver, initial, operation) {
        var index = _.kotlin.get_size_1($receiver) - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver.get_za3lpa$(index--), accumulator);
        }
        return accumulator;
      },
      foldRight_xn4ira$: function ($receiver, initial, operation) {
        var index = _.kotlin.get_size_0($receiver) - 1;
        var accumulator = initial;
        while (index >= 0) {
          accumulator = operation($receiver.charAt(index--), accumulator);
        }
        return accumulator;
      },
      forEach_de9h66$: function ($receiver, operation) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            operation(element);
          }
        }
      },
      forEach_50zxbw$: function ($receiver, operation) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_x245au$: function ($receiver, operation) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_h5ed0c$: function ($receiver, operation) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_24jijj$: function ($receiver, operation) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_im8pe8$: function ($receiver, operation) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_1xntkt$: function ($receiver, operation) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            operation(element);
          }
        }
      },
      forEach_3cuuyy$: function ($receiver, operation) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_p67zio$: function ($receiver, operation) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_vqr6wr$: function ($receiver, operation) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_gld13f$: function ($receiver, operation) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_9fpnal$: function ($receiver, operation) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      forEach_t73kuc$: function ($receiver, operation) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            operation(element);
          }
        }
      },
      max_2hx8bi$: function ($receiver) {
        if (_.kotlin.isEmpty_2hx8bi$($receiver))
          return null;
        var max = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_7($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (max < e)
              max = e;
          }
        }
        return max;
      },
      max_964n92$: function ($receiver) {
        if (_.kotlin.isEmpty_964n92$($receiver))
          return null;
        var max = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_0($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (max < e)
              max = e;
          }
        }
        return max;
      },
      max_355nu0$: function ($receiver) {
        if (_.kotlin.isEmpty_355nu0$($receiver))
          return null;
        var max = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_6($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (max < e)
              max = e;
          }
        }
        return max;
      },
      max_bvy38t$: function ($receiver) {
        if (_.kotlin.isEmpty_bvy38t$($receiver))
          return null;
        var max = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_5($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (max < e)
              max = e;
          }
        }
        return max;
      },
      max_rjqrz0$: function ($receiver) {
        if (_.kotlin.isEmpty_rjqrz0$($receiver))
          return null;
        var max = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_4($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (max < e)
              max = e;
          }
        }
        return max;
      },
      max_tmsbgp$: function ($receiver) {
        if (_.kotlin.isEmpty_tmsbgp$($receiver))
          return null;
        var max = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_2($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (max < e)
              max = e;
          }
        }
        return max;
      },
      max_se6h4y$: function ($receiver) {
        if (_.kotlin.isEmpty_se6h4y$($receiver))
          return null;
        var max = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_3($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (max < e)
              max = e;
          }
        }
        return max;
      },
      max_i2lc78$: function ($receiver) {
        if (_.kotlin.isEmpty_i2lc78$($receiver))
          return null;
        var max = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_1($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (max < e)
              max = e;
          }
        }
        return max;
      },
      max_h3panj$: function ($receiver) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          return null;
        var max = iterator.next();
        while (iterator.hasNext()) {
          var e = iterator.next();
          if (max < e)
            max = e;
        }
        return max;
      },
      max_pdnvbz$: function ($receiver) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          return null;
        var max = iterator.next();
        while (iterator.hasNext()) {
          var e = iterator.next();
          if (max < e)
            max = e;
        }
        return max;
      },
      max_pdl1w0$: function ($receiver) {
        var iterator = _.kotlin.iterator_gw00vq$($receiver);
        if (!iterator.hasNext())
          return null;
        var max = iterator.next();
        while (iterator.hasNext()) {
          var e = iterator.next();
          if (max < e)
            max = e;
        }
        return max;
      },
      maxBy_de9h66$: function ($receiver, f) {
        if (_.kotlin.isEmpty_2hx8bi$($receiver))
          return null;
        var maxElem = $receiver[0];
        var maxValue = f(maxElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_7($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (maxValue < v) {
              maxElem = e;
              maxValue = v;
            }
          }
        }
        return maxElem;
      },
      maxBy_50zxbw$: function ($receiver, f) {
        if (_.kotlin.isEmpty_l1lu5s$($receiver))
          return null;
        var maxElem = $receiver[0];
        var maxValue = f(maxElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (maxValue < v) {
              maxElem = e;
              maxValue = v;
            }
          }
        }
        return maxElem;
      },
      maxBy_x245au$: function ($receiver, f) {
        if (_.kotlin.isEmpty_964n92$($receiver))
          return null;
        var maxElem = $receiver[0];
        var maxValue = f(maxElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_0($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (maxValue < v) {
              maxElem = e;
              maxValue = v;
            }
          }
        }
        return maxElem;
      },
      maxBy_h5ed0c$: function ($receiver, f) {
        if (_.kotlin.isEmpty_355nu0$($receiver))
          return null;
        var maxElem = $receiver[0];
        var maxValue = f(maxElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_6($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (maxValue < v) {
              maxElem = e;
              maxValue = v;
            }
          }
        }
        return maxElem;
      },
      maxBy_24jijj$: function ($receiver, f) {
        if (_.kotlin.isEmpty_bvy38t$($receiver))
          return null;
        var maxElem = $receiver[0];
        var maxValue = f(maxElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_5($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (maxValue < v) {
              maxElem = e;
              maxValue = v;
            }
          }
        }
        return maxElem;
      },
      maxBy_im8pe8$: function ($receiver, f) {
        if (_.kotlin.isEmpty_rjqrz0$($receiver))
          return null;
        var maxElem = $receiver[0];
        var maxValue = f(maxElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_4($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (maxValue < v) {
              maxElem = e;
              maxValue = v;
            }
          }
        }
        return maxElem;
      },
      maxBy_1xntkt$: function ($receiver, f) {
        if (_.kotlin.isEmpty_tmsbgp$($receiver))
          return null;
        var maxElem = $receiver[0];
        var maxValue = f(maxElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_2($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (maxValue < v) {
              maxElem = e;
              maxValue = v;
            }
          }
        }
        return maxElem;
      },
      maxBy_3cuuyy$: function ($receiver, f) {
        if (_.kotlin.isEmpty_se6h4y$($receiver))
          return null;
        var maxElem = $receiver[0];
        var maxValue = f(maxElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_3($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (maxValue < v) {
              maxElem = e;
              maxValue = v;
            }
          }
        }
        return maxElem;
      },
      maxBy_p67zio$: function ($receiver, f) {
        if (_.kotlin.isEmpty_i2lc78$($receiver))
          return null;
        var maxElem = $receiver[0];
        var maxValue = f(maxElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_1($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (maxValue < v) {
              maxElem = e;
              maxValue = v;
            }
          }
        }
        return maxElem;
      },
      maxBy_vqr6wr$: function ($receiver, f) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          return null;
        var maxElem = iterator.next();
        var maxValue = f(maxElem);
        while (iterator.hasNext()) {
          var e = iterator.next();
          var v = f(e);
          if (maxValue < v) {
            maxElem = e;
            maxValue = v;
          }
        }
        return maxElem;
      },
      maxBy_9fpnal$: function ($receiver, f) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          return null;
        var maxElem = iterator.next();
        var maxValue = f(maxElem);
        while (iterator.hasNext()) {
          var e = iterator.next();
          var v = f(e);
          if (maxValue < v) {
            maxElem = e;
            maxValue = v;
          }
        }
        return maxElem;
      },
      maxBy_t73kuc$: function ($receiver, f) {
        var iterator = _.kotlin.iterator_gw00vq$($receiver);
        if (!iterator.hasNext())
          return null;
        var maxElem = iterator.next();
        var maxValue = f(maxElem);
        while (iterator.hasNext()) {
          var e = iterator.next();
          var v = f(e);
          if (maxValue < v) {
            maxElem = e;
            maxValue = v;
          }
        }
        return maxElem;
      },
      maxBy_gld13f$: function ($receiver, f) {
        var iterator = _.kotlin.iterator_s8ckw1$($receiver);
        if (!iterator.hasNext())
          return null;
        var maxElem = iterator.next();
        var maxValue = f(maxElem);
        while (iterator.hasNext()) {
          var e = iterator.next();
          var v = f(e);
          if (maxValue < v) {
            maxElem = e;
            maxValue = v;
          }
        }
        return maxElem;
      },
      min_2hx8bi$: function ($receiver) {
        if (_.kotlin.isEmpty_2hx8bi$($receiver))
          return null;
        var min = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_7($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (min > e)
              min = e;
          }
        }
        return min;
      },
      min_964n92$: function ($receiver) {
        if (_.kotlin.isEmpty_964n92$($receiver))
          return null;
        var min = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_0($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (min > e)
              min = e;
          }
        }
        return min;
      },
      min_355nu0$: function ($receiver) {
        if (_.kotlin.isEmpty_355nu0$($receiver))
          return null;
        var min = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_6($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (min > e)
              min = e;
          }
        }
        return min;
      },
      min_bvy38t$: function ($receiver) {
        if (_.kotlin.isEmpty_bvy38t$($receiver))
          return null;
        var min = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_5($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (min > e)
              min = e;
          }
        }
        return min;
      },
      min_rjqrz0$: function ($receiver) {
        if (_.kotlin.isEmpty_rjqrz0$($receiver))
          return null;
        var min = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_4($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (min > e)
              min = e;
          }
        }
        return min;
      },
      min_tmsbgp$: function ($receiver) {
        if (_.kotlin.isEmpty_tmsbgp$($receiver))
          return null;
        var min = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_2($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (min > e)
              min = e;
          }
        }
        return min;
      },
      min_se6h4y$: function ($receiver) {
        if (_.kotlin.isEmpty_se6h4y$($receiver))
          return null;
        var min = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_3($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (min > e)
              min = e;
          }
        }
        return min;
      },
      min_i2lc78$: function ($receiver) {
        if (_.kotlin.isEmpty_i2lc78$($receiver))
          return null;
        var min = $receiver[0];
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_1($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            if (min > e)
              min = e;
          }
        }
        return min;
      },
      min_h3panj$: function ($receiver) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          return null;
        var min = iterator.next();
        while (iterator.hasNext()) {
          var e = iterator.next();
          if (min > e)
            min = e;
        }
        return min;
      },
      min_pdnvbz$: function ($receiver) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          return null;
        var min = iterator.next();
        while (iterator.hasNext()) {
          var e = iterator.next();
          if (min > e)
            min = e;
        }
        return min;
      },
      min_pdl1w0$: function ($receiver) {
        var iterator = _.kotlin.iterator_gw00vq$($receiver);
        if (!iterator.hasNext())
          return null;
        var min = iterator.next();
        while (iterator.hasNext()) {
          var e = iterator.next();
          if (min > e)
            min = e;
        }
        return min;
      },
      minBy_de9h66$: function ($receiver, f) {
        if ($receiver.length === 0)
          return null;
        var minElem = $receiver[0];
        var minValue = f(minElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_7($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (minValue > v) {
              minElem = e;
              minValue = v;
            }
          }
        }
        return minElem;
      },
      minBy_50zxbw$: function ($receiver, f) {
        if ($receiver.length === 0)
          return null;
        var minElem = $receiver[0];
        var minValue = f(minElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (minValue > v) {
              minElem = e;
              minValue = v;
            }
          }
        }
        return minElem;
      },
      minBy_x245au$: function ($receiver, f) {
        if ($receiver.length === 0)
          return null;
        var minElem = $receiver[0];
        var minValue = f(minElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_0($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (minValue > v) {
              minElem = e;
              minValue = v;
            }
          }
        }
        return minElem;
      },
      minBy_h5ed0c$: function ($receiver, f) {
        if ($receiver.length === 0)
          return null;
        var minElem = $receiver[0];
        var minValue = f(minElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_6($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (minValue > v) {
              minElem = e;
              minValue = v;
            }
          }
        }
        return minElem;
      },
      minBy_24jijj$: function ($receiver, f) {
        if ($receiver.length === 0)
          return null;
        var minElem = $receiver[0];
        var minValue = f(minElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_5($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (minValue > v) {
              minElem = e;
              minValue = v;
            }
          }
        }
        return minElem;
      },
      minBy_im8pe8$: function ($receiver, f) {
        if ($receiver.length === 0)
          return null;
        var minElem = $receiver[0];
        var minValue = f(minElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_4($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (minValue > v) {
              minElem = e;
              minValue = v;
            }
          }
        }
        return minElem;
      },
      minBy_1xntkt$: function ($receiver, f) {
        if ($receiver.length === 0)
          return null;
        var minElem = $receiver[0];
        var minValue = f(minElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_2($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (minValue > v) {
              minElem = e;
              minValue = v;
            }
          }
        }
        return minElem;
      },
      minBy_3cuuyy$: function ($receiver, f) {
        if ($receiver.length === 0)
          return null;
        var minElem = $receiver[0];
        var minValue = f(minElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_3($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (minValue > v) {
              minElem = e;
              minValue = v;
            }
          }
        }
        return minElem;
      },
      minBy_p67zio$: function ($receiver, f) {
        if ($receiver.length === 0)
          return null;
        var minElem = $receiver[0];
        var minValue = f(minElem);
        var tmp$0;
        {
          tmp$0 = _.kotlin.get_lastIndex_1($receiver) + 1;
          for (var i = 1; i !== tmp$0; i++) {
            var e = $receiver[i];
            var v = f(e);
            if (minValue > v) {
              minElem = e;
              minValue = v;
            }
          }
        }
        return minElem;
      },
      minBy_vqr6wr$: function ($receiver, f) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          return null;
        var minElem = iterator.next();
        var minValue = f(minElem);
        while (iterator.hasNext()) {
          var e = iterator.next();
          var v = f(e);
          if (minValue > v) {
            minElem = e;
            minValue = v;
          }
        }
        return minElem;
      },
      minBy_9fpnal$: function ($receiver, f) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          return null;
        var minElem = iterator.next();
        var minValue = f(minElem);
        while (iterator.hasNext()) {
          var e = iterator.next();
          var v = f(e);
          if (minValue > v) {
            minElem = e;
            minValue = v;
          }
        }
        return minElem;
      },
      minBy_t73kuc$: function ($receiver, f) {
        var iterator = _.kotlin.iterator_gw00vq$($receiver);
        if (!iterator.hasNext())
          return null;
        var minElem = iterator.next();
        var minValue = f(minElem);
        while (iterator.hasNext()) {
          var e = iterator.next();
          var v = f(e);
          if (minValue > v) {
            minElem = e;
            minValue = v;
          }
        }
        return minElem;
      },
      minBy_gld13f$: function ($receiver, f) {
        var iterator = _.kotlin.iterator_s8ckw1$($receiver);
        if (!iterator.hasNext())
          return null;
        var minElem = iterator.next();
        var minValue = f(minElem);
        while (iterator.hasNext()) {
          var e = iterator.next();
          var v = f(e);
          if (minValue > v) {
            minElem = e;
            minValue = v;
          }
        }
        return minElem;
      },
      none_2hx8bi$: function ($receiver) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            return false;
          }
        }
        return true;
      },
      none_l1lu5s$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_964n92$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_355nu0$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_bvy38t$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_rjqrz0$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_tmsbgp$: function ($receiver) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            return false;
          }
        }
        return true;
      },
      none_se6h4y$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_i2lc78$: function ($receiver) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_h3panj$: function ($receiver) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_s8ckw1$: function ($receiver) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_pdnvbz$: function ($receiver) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_pdl1w0$: function ($receiver) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            return false;
          }
        }
        return true;
      },
      none_de9h66$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_50zxbw$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_x245au$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_h5ed0c$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_24jijj$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_im8pe8$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_1xntkt$: function ($receiver, predicate) {
        var tmp$0, tmp$1, tmp$2;
        {
          tmp$0 = $receiver, tmp$1 = tmp$0.length;
          for (var tmp$2 = 0; tmp$2 !== tmp$1; ++tmp$2) {
            var element = tmp$0[tmp$2];
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_3cuuyy$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_p67zio$: function ($receiver, predicate) {
        {
          var tmp$0 = Kotlin.arrayIterator($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_vqr6wr$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_gld13f$: function ($receiver, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_s8ckw1$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_9fpnal$: function ($receiver, predicate) {
        {
          var tmp$0 = $receiver.iterator();
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      none_t73kuc$: function ($receiver, predicate) {
        {
          var tmp$0 = _.kotlin.iterator_gw00vq$($receiver);
          while (tmp$0.hasNext()) {
            var element = tmp$0.next();
            if (predicate(element))
              return false;
          }
        }
        return true;
      },
      reduce_de9h67$: function ($receiver, operation) {
        var iterator = Kotlin.arrayIterator($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_50zxbx$: function ($receiver, operation) {
        var iterator = Kotlin.arrayIterator($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_x245av$: function ($receiver, operation) {
        var iterator = Kotlin.arrayIterator($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_h5ed0b$: function ($receiver, operation) {
        var iterator = Kotlin.arrayIterator($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_24jijk$: function ($receiver, operation) {
        var iterator = Kotlin.arrayIterator($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_im8pe7$: function ($receiver, operation) {
        var iterator = Kotlin.arrayIterator($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_1xntks$: function ($receiver, operation) {
        var iterator = Kotlin.arrayIterator($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_3cuuyz$: function ($receiver, operation) {
        var iterator = Kotlin.arrayIterator($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_p67zip$: function ($receiver, operation) {
        var iterator = Kotlin.arrayIterator($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_vqr6ws$: function ($receiver, operation) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_9fpnam$: function ($receiver, operation) {
        var iterator = $receiver.iterator();
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduce_t73kub$: function ($receiver, operation) {
        var iterator = _.kotlin.iterator_gw00vq$($receiver);
        if (!iterator.hasNext())
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = iterator.next();
        while (iterator.hasNext()) {
          accumulator = operation(accumulator, iterator.next());
        }
        return accumulator;
      },
      reduceRight_de9h67$: function ($receiver, operation) {
        var index = $receiver.length - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver[index--];
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      reduceRight_50zxbx$: function ($receiver, operation) {
        var index = $receiver.length - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver[index--];
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      reduceRight_x245av$: function ($receiver, operation) {
        var index = $receiver.length - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver[index--];
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      reduceRight_h5ed0b$: function ($receiver, operation) {
        var index = $receiver.length - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver[index--];
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      reduceRight_24jijk$: function ($receiver, operation) {
        var index = $receiver.length - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver[index--];
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      reduceRight_im8pe7$: function ($receiver, operation) {
        var index = $receiver.length - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver[index--];
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      reduceRight_1xntks$: function ($receiver, operation) {
        var index = $receiver.length - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver[index--];
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      reduceRight_3cuuyz$: function ($receiver, operation) {
        var index = $receiver.length - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver[index--];
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      reduceRight_p67zip$: function ($receiver, operation) {
        var index = $receiver.length - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver[index--];
        while (index >= 0) {
          accumulator = operation($receiver[index--], accumulator);
        }
        return accumulator;
      },
      reduceRight_7bxqi8$: function ($receiver, operation) {
        var index = _.kotlin.get_size_1($receiver) - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver.get_za3lpa$(index--);
        while (index >= 0) {
          accumulator = operation($receiver.get_za3lpa$(index--), accumulator);
        }
        return accumulator;
      },
      reduceRight_t73kub$: function ($receiver, operation) {
        var index = _.kotlin.get_size_0($receiver) - 1;
        if (index < 0)
          throw new Kotlin.UnsupportedOperationException("Empty iterable can't be reduced");
        var accumulator = $receiver.charAt(index--);
        while (index >= 0) {
          accumulator = operation($receiver.charAt(index--), accumulator);
        }
        return accumulator;
      },
      support: Kotlin.definePackage(function () {
        this.State = Kotlin.createObject(null, function () {
          this.Ready = 0;
          this.NotReady = 1;
          this.Done = 2;
          this.Failed = 3;
        });
      }, /** @lends _.kotlin.support */ {
        AbstractIterator: Kotlin.createClass(function () {
          return [Kotlin.Iterator];
        }, function () {
          this.state_xrvatb$ = _.kotlin.support.State.NotReady;
          this.nextValue_u0jzfw$ = null;
        }, /** @lends _.kotlin.support.AbstractIterator.prototype */ {
          hasNext: function () {
            _.kotlin.require_eltq40$(this.state_xrvatb$ !== _.kotlin.support.State.Failed);
            var tmp$0 = this.state_xrvatb$, tmp$1;
            if (tmp$0 === _.kotlin.support.State.Done)
              tmp$1 = false;
            else if (tmp$0 === _.kotlin.support.State.Ready)
              tmp$1 = true;
            else
              tmp$1 = this.tryToComputeNext();
            return tmp$1;
          },
          next: function () {
            if (!this.hasNext())
              throw new Kotlin.NoSuchElementException();
            this.state_xrvatb$ = _.kotlin.support.State.NotReady;
            return this.nextValue_u0jzfw$;
          },
          peek: function () {
            if (!this.hasNext())
              throw new Kotlin.NoSuchElementException();
            return this.nextValue_u0jzfw$;
          },
          tryToComputeNext: function () {
            this.state_xrvatb$ = _.kotlin.support.State.Failed;
            this.computeNext();
            return this.state_xrvatb$ === _.kotlin.support.State.Ready;
          },
          setNext_za3rmp$: function (value) {
            this.nextValue_u0jzfw$ = value;
            this.state_xrvatb$ = _.kotlin.support.State.Ready;
          },
          done: function () {
            this.state_xrvatb$ = _.kotlin.support.State.Done;
          }
        })
      })
    })
  });
  Kotlin.defineModule('pcm', _);
}(Kotlin));
if(typeof(module)!='undefined'){module.exports = Kotlin.modules['pcm'];}
