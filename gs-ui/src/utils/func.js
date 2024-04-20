import Vue from 'vue'

export function newFunction(funcName, funcParam, funcScript) {
  const func = new Function(funcParam, funcScript)
  Vue.prototype[funcName] = func
}
