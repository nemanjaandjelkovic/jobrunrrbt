export function JobCreateReducer(jobs, action) {
    switch (action.type) {
        case 'add': {
            let componentId = jobs.length > 0 ? parseInt(jobs[jobs.length - 1].rowId) + 1 : 0
            return [
                ...jobs,
                {
                    rowId: componentId,
                    key: componentId,
                    jobData: {
                        jobSignature: "",
                        jobArg: [],
                        jobDate: ""
                    }
                },
            ];
        }
        case 'delete': {
            return jobs.filter(item => item.rowId !== action.id)
        }
        default: {
            throw Error("Action is unavailable " + action.type)
        }
    }
}