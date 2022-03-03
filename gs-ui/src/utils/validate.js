/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
}

/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}

/**
 * 校验字节长度，不指定message时根据min和max自动生成提示，不指定min和max时默认0-50个字节
 * @param rule
 * @param value
 * @param callback
 */
export function validateByteSize(rule, value, callback) {
  if (!value) {
    callback()
    return
  }

  // 有值再校验
  value = value.replace(/[^\u0000-\u00ff]/g,"***") // 中文及中文类字符转为3个字节，和Java UTF8下的字符长度保持一致
  let min = rule.min || 0
  let max = rule.max || 50
  if (value.length < min || value.length > max) {
    if (rule.message) {
      callback(new Error(rule.message))
    } else {
      if (min > 0) {
        callback(new Error('长度在 ' + min + ' 到  '+ max + ' 个字节'))
      } else {
        callback(new Error('长度不超过 ' + max + ' 个字节'))
      }
    }
  } else {
    callback()
  }
}

/**
 * 校验数字，不指定message时根据min和max自动生成提示，不指定min和max时默认0-50
 * @param rule
 * @param value
 * @param callback
 */
export function validateNumber(rule, value, callback) {
  const reg = /^[0-9]+$/
  if (!value) {
    callback()
    return
  }

  // 有值再校验
  if (!reg.test(value))
    return callback(new Error('请输入有效数字'))

  let min = rule.min || 0
  let max = rule.max || 50
  if (value < min || value > max) {
    if (rule.message) {
      callback(new Error(rule.message))
    } else {
      if (min > 0) {
        callback(new Error('数字范围在 ' + min + ' 到  '+ max))
      } else {
        callback(new Error('数字不能大于 ' + max))
      }
    }
  } else {
    callback()
  }
}
