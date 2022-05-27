// 把图片转成base64编码
export function getBase64(file) {
  return new Promise(function (resolve, reject) {
    let reader = new FileReader();
    let imgResult = "";
    reader.readAsDataURL(file);
    reader.onload = function (e) {
      imgResult = e.target.result; // 这种写法可兼容移动端
    };
    reader.onerror = function (error) {
      reject(error);
    };
    reader.onloadend = function () {
      let item = ";base64,";
      imgResult = imgResult.substring(imgResult.indexOf(item) + item.length);
      resolve(imgResult);
    }
  })
}

// 获取文件后缀
export function getFileType(fileName) {
  let spotIndex = fileName.lastIndexOf(".");
  let fileLength = fileName.length;
  let fileType = fileName.substring(spotIndex, fileLength);
  return fileType;
}


// 下载文件，base64图片下载
export function downloadBase64Photo(base64Photo, fileName) {
  var byteCharacters = atob(base64Photo);
  var byteNumbers = new Array(byteCharacters.length);
  for (var i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  var byteArray = new Uint8Array(byteNumbers);
  var blob = new Blob([byteArray], {
    type: undefined,
  });
  var a = document.createElement('a')
  var url = window.URL.createObjectURL(blob)
  a.href = url
  a.download = fileName // 文件名
  a.click()

  // var xhr = new XMLHttpRequest()
  // xhr.open('get', baseUrl, true) // get、post都可
  // xhr.responseType = 'blob' // 转换流
  // xhr.onload = function() {
  //   if (this.status == 200) {
  //     var blob = this.response
  //
  //   }
  //   a.click()
  //   window.URL.revokeObjectURL(url)
  // }
  // xhr.send()

}
