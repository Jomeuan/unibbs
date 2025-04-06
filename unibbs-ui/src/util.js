export function getToken() {
    // localStorage.removeItem('token')
    return localStorage.getItem('token') ? localStorage.getItem('token') : ""
    // return "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQXV0aGVudGljYXRpb24iOnsidXNlciI6eyJpZCI6MTg4Nzc1MzA3ODQ0MjgyMzY4MiwiYWNjb3VudCI6IjEyNDYiLCJwYXNzd29yZCI6bnVsbCwic3RhdGUiOm51bGwsImV4aXByYXRpb24iOm51bGx9LCJyb2xlcyI6W3siaWQiOjIsIm5hbWUiOiJWSVNJVE9SIn0seyJpZCI6MywibmFtZSI6Ik1PREVSQVRPUiJ9XX19.j7FbMCne4ZdCrloWkQ0ORsBAOkqHwGKEvbULnu5jQEI"
}

var authCache
// 返回{user:userPO,roles:[rolePo...]}
export function getAuth() {
    return new Promise(resolve => {
        if (authCache) {
            // console.log(authCache)
            // resolve的参数就是then((参数)=>{})里面的参数
            resolve(authCache)
        }
        else {
            if (!getToken()) {
                resolve({
                    user: {
                        id: "",
                        account: null,
                        password: null,
                        state: null,
                        exipration: null
                    },
                    roles: []
                })
            } else {
                fetch("http://localhost:9999/security/auth/parseJWT", {
                    method: "GET",
                    headers: { token: getToken() }
                })
                    .then(response => {
                        if (response.ok) {
                            return response.json()
                        } else return {
                            user: {
                                id: "",
                                account: null,
                                password: null,
                                state: null,
                                exipration: null
                            },
                            roles: [
                                {
                                    id: null,
                                    name: null
                                },
                            ]
                        };
                    })
                    .then(data => {
                        // localStorage.setItem('auth', data)
                        authCache = data
                        resolve(data)
                    })
            }
        }
    })
}

